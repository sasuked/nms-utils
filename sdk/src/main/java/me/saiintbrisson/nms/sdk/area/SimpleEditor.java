package me.saiintbrisson.nms.sdk.area;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.saiintbrisson.nms.api.area.AreaEditor;
import me.saiintbrisson.nms.api.area.AreaHistory;
import me.saiintbrisson.nms.api.area.cuboid.ChunkPosition;
import me.saiintbrisson.nms.api.area.cuboid.Cuboid;
import me.saiintbrisson.nms.sdk.NmsPlugin;
import me.saiintbrisson.nms.sdk.area.object.NmsBlock;
import me.saiintbrisson.nms.sdk.area.object.NmsWorld;
import net.minecraft.server.v1_8_R3.*;
import org.spigotmc.AsyncCatcher;

import java.util.*;

@RequiredArgsConstructor
public class SimpleEditor implements AreaEditor<NmsBlock, NmsWorld> {

    private final NmsWorld world;

    private final Set<ChunkPosition> chunks = new HashSet<>();
    private final Map<net.minecraft.server.v1_8_R3.BlockPosition, int[]> changes = new HashMap<>();

    @Setter
    private boolean ignoreHistory;
    @Setter
    private AreaHistory<?> history;

    @Getter
    private int commitChanges = 0;

    @Getter
    private long commitPeriod;

    @Override
    public int getChangesSize() {
        return changes.size();
    }

    @Override
    public void setBlock(me.saiintbrisson.nms.api.area.BlockPosition position, int id, byte data) {
        setBlock(
          new net.minecraft.server.v1_8_R3.BlockPosition(
            position.getX(),
            position.getY(),
            position.getZ()
          ),
          id,
          data
        );
    }

    public void setBlock(net.minecraft.server.v1_8_R3.BlockPosition position, int id, byte data) {
        setBlock(position, new int[]{id, data});
    }

    public void setBlock(net.minecraft.server.v1_8_R3.BlockPosition position, int[] info) {
        chunks.add(new ChunkPosition(
          (short) (position.getX() >> 4),
          (short) (position.getY() >> 4)
        ));

        changes.put(
          position,
          info
        );
    }

    @Override
    public void setBlocks(Cuboid<NmsBlock> cuboid, int id, byte data) {
        chunks.addAll(Arrays.asList(cuboid.getChunks()));

        for(me.saiintbrisson.nms.api.area.BlockPosition position : cuboid.positionIterator()) {
            changes.put(NmsPlugin.toNMSPosition(position), new int[]{id, data});
        }
    }

    @Override
    public void commit() {
        commitPeriod = System.currentTimeMillis();

        final ChunkProviderServer providerServer = world.getChunkProviderServer();
        final WorldServer handle = world.getHandle();

        AsyncCatcher.enabled = false;

        Map<ChunkPosition, Chunk> chunks = new HashMap<>();
        for(ChunkPosition chunk : this.chunks) {
            short x = chunk.getX();
            short z = chunk.getZ();

            final Chunk orCreateChunk = providerServer.getOrCreateChunk(x, z);
            chunks.put(chunk, orCreateChunk);
        }

        Map<net.minecraft.server.v1_8_R3.BlockPosition, int[]> old = null;
        if(!ignoreHistory && history != null && history instanceof NmsHistory) {
            old = new HashMap<>();
        }

        commitChanges = 0;
        for(Map.Entry<net.minecraft.server.v1_8_R3.BlockPosition, int[]> entry : changes.entrySet()) {
            final int[] value = entry.getValue();
            final net.minecraft.server.v1_8_R3.BlockPosition key = entry.getKey();

            final ChunkPosition position = new ChunkPosition(key.getX() >> 4, key.getZ() >> 4);
            Chunk chunk = chunks.get(position);

            if(chunk == null) {
                chunk = handle.getChunkAt(position.getX(), position.getZ());
                chunks.put(position, chunk);
            }

            int combined = value[0] + (value[1] << 12);
            IBlockData blockData = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);

            int x = key.getX() & 15;
            int y = key.getY();
            int z = key.getZ() & 15;

            ChunkSection chunksection = chunk.getSections()[y >> 4];
            if(chunksection == null) {
                if(blockData.getBlock() == Blocks.AIR) {
                    continue;
                }

                chunksection
                  = chunk.getSections()[y >> 4]
                  = new ChunkSection(y >> 4 << 4, !handle.worldProvider.o());
            }

            y = y & 15;

            IBlockData type = chunksection.getType(x, y, z);
            Block oldBlock = type.getBlock();
            Block newBlock = blockData.getBlock();

            if(type.equals(blockData)) continue;

            if(old != null) {
                old.put(key, new int[]{
                  net.minecraft.server.v1_8_R3.Block.getId(oldBlock),
                  oldBlock.toLegacyData(type)
                });
            }

            chunksection.setType(x, y, z, blockData);

            if(newBlock.p() != oldBlock.p() || newBlock.r() != oldBlock.r()) {
                if(!handle.worldProvider.o()) {
                    handle.c(EnumSkyBlock.SKY, key);
                }

                handle.c(EnumSkyBlock.BLOCK, key);
            }

            handle.notifyAndUpdatePhysics(key, chunk, oldBlock, newBlock, 2);

            commitChanges++;
        }

        for(Chunk value : chunks.values()) {
            value.initLighting();
        }

        if(!ignoreHistory && history != null && history instanceof NmsHistory) {
            ((NmsHistory) history).addChange(old);
        }
        changes.clear();

        commitPeriod = System.currentTimeMillis() - commitPeriod;
    }

    @Override
    public boolean undo() {
        Map<?, int[]> lastChange = history.getLastChange();
        if(lastChange == null) return false;

        for(Map.Entry<?, int[]> entry : lastChange.entrySet()) {
            setBlock((net.minecraft.server.v1_8_R3.BlockPosition) entry.getKey(), entry.getValue());
        }

        return true;
    }


}
