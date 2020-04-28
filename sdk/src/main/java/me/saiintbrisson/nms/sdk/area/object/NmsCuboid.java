package me.saiintbrisson.nms.sdk.area.object;

import lombok.Getter;
import me.saiintbrisson.nms.api.area.cuboid.Cuboid;
import me.saiintbrisson.nms.sdk.area.NmsIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Getter
public class NmsCuboid implements Cuboid<NmsBlock> {

    private NmsWorld world;

    private int lowerX, lowerY, lowerZ,
        upperX, upperY, upperZ;

    public NmsCuboid(NmsWorld world,
                     int lowerX, int lowerY, int lowerZ,
                     int upperX, int upperY, int upperZ) {
        this.world = world;
        this.lowerX = Math.min(lowerX, upperX);
        this.lowerY = Math.min(lowerY, upperY);
        this.lowerZ = Math.min(lowerZ, upperZ);
        this.upperX = Math.max(lowerX, upperX);
        this.upperY = Math.max(lowerY, upperY);
        this.upperZ = Math.max(lowerZ, upperZ);
    }

    @Override
    public NmsBlock[] getBorder(int y) {
        Set<NmsBlock> blocks = new HashSet<>();

        for(int i = lowerX; i < upperX; i++) {
            blocks.add(world.getBlock(i, y, lowerZ));
            blocks.add(world.getBlock(i, y, upperZ));
        }

        for(int i = lowerZ; i < upperZ; i++) {
            blocks.add(world.getBlock(lowerX, y, i));
            blocks.add(world.getBlock(upperX, y, i));
        }

        return blocks.toArray(new NmsBlock[0]);
    }

    @Override
    public Iterator<NmsBlock> iterator() {
        return new NmsIterator(
            world,
            lowerX, lowerY, lowerZ,
            upperX, upperY, upperZ
        );
    }

}
