package me.saiintbrisson.nms.sdk.area.object;

import lombok.Getter;
import me.saiintbrisson.nms.api.area.AreaWorld;
import me.saiintbrisson.nms.api.area.cuboid.Cuboid;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.material.MaterialData;
import org.spigotmc.AsyncCatcher;

@Getter
public class NmsWorld implements AreaWorld<NmsBlock> {

    public WorldServer handle;

    public NmsWorld(org.bukkit.World world) {
        this.handle = ((CraftWorld) world).getHandle();
    }

    public ChunkProviderServer getChunkProviderServer() {
        return handle.chunkProviderServer;
    }

    public boolean isChunkLoaded(int x, int z) {
        return handle.chunkProviderServer.isChunkLoaded(x, z);
    }

    @Override
    public NmsBlock getBlock(int x, int y, int z) {
        return new NmsBlock(this, x, y, z);
    }

    public int getType(int x, int y, int z) {
        return net.minecraft.server.v1_8_R3.Block.getId(
            handle.getChunkAt(x >> 4, z >> 4).getType(
                new BlockPosition(x, y, z)
            )
        );
    }

    public byte getData(int x, int y, int z) {
        net.minecraft.server.v1_8_R3.Chunk chunk = handle.getChunkAt(x >> 4, z >> 4);

        BlockPosition position = new BlockPosition(x, y, z);

        return (byte) chunk.getType(position)
            .toLegacyData(chunk.getBlockData(position));
    }

    public MaterialData getTypeAndData(int x, int y, int z) {
        net.minecraft.server.v1_8_R3.Chunk chunk = handle.getChunkAt(x >> 4, z >> 4);

        BlockPosition position = new BlockPosition(x, y, z);
        net.minecraft.server.v1_8_R3.Block block = chunk.getType(position);

        return new MaterialData(
            net.minecraft.server.v1_8_R3.Block.getId(block),
            (byte) block.toLegacyData(chunk.getBlockData(position))
        );
    }

    public int[] getRawTypeAndData(Chunk chunk, BlockPosition position) {
        net.minecraft.server.v1_8_R3.Block block = chunk.getType(position);

        return new int[] {
            net.minecraft.server.v1_8_R3.Block.getId(block),
            block.toLegacyData(chunk.getBlockData(position))
        };
    }

    @Override
    public Cuboid<NmsBlock> getRegion(int lowerX, int lowerY, int lowerZ,
                                      int upperX, int upperY, int upperZ) {
        return null;
    }

    @Override
    public void setBlock(int x, int y, int z, int id, byte data) {
        AsyncCatcher.enabled = false;

        BlockPosition position = new BlockPosition(x, y, z);
        net.minecraft.server.v1_8_R3.Chunk chunk = handle.getChunkAt(x >> 4, z >> 4);
        setRawBlock(chunk, position, id, data);
        handle.notify(position);
    }

    public void setBlock(BlockPosition position, int id, byte data) {
        net.minecraft.server.v1_8_R3.Chunk chunk = handle.getChunkAt(
            position.getX() >> 4,
            position.getZ() >> 4
        );
        int combined = id + (data << 12);
        IBlockData blockData = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);

        handle.setTypeAndData(position, blockData, 2);
        chunk.a(position, blockData);
        handle.notify(position);
    }

    public void setRawBlock(Chunk chunk, BlockPosition position, int id, byte data) {
        int combined = id + (data << 12);
        IBlockData blockData = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);

        handle.setTypeAndData(position, blockData, 2);
        chunk.a(position, blockData);
    }

}
