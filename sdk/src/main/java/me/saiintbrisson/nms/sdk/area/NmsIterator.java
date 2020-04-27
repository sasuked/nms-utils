package me.saiintbrisson.nms.sdk.area;

import me.saiintbrisson.nms.sdk.area.object.NmsBlock;
import me.saiintbrisson.nms.sdk.area.object.NmsWorld;

import java.util.Iterator;

public class NmsIterator implements Iterator<NmsBlock> {

    private NmsWorld world;

    private int baseX, baseY, baseZ;

    private int x, y, z;

    private int sizeX, sizeY, sizeZ;

    public NmsIterator(NmsWorld world,
                       int lowerX, int lowerY, int lowerZ,
                       int upperX, int upperY, int upperZ) {
        this.world = world;

        this.baseX = lowerX;
        this.baseY = lowerY;
        this.baseZ = lowerZ;

        this.sizeX = Math.abs(upperX - lowerX) + 1;
        this.sizeY = Math.abs(upperY - lowerY) + 1;
        this.sizeZ = Math.abs(upperZ - lowerZ) + 1;

        this.x = this.y = this.z = 0;
    }

    public boolean hasNext() {
        return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
    }

    public NmsBlock next() {
        if(!world.isChunkLoaded(baseX + x, baseZ + z)) {

        }

        NmsBlock block = world.getBlock(baseX + x, baseY + y, baseZ + z);

        if(++x >= this.sizeX) {
            this.x = 0;
            if(++this.y >= this.sizeY) {
                this.y = 0;
                ++this.z;
            }
        }

        return block;
    }

}
