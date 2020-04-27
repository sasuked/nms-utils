package me.saiintbrisson.nms.api.area.cuboid;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;

public class SimpleIterator implements Iterator<Block> {

    private World world;

    private int baseX, baseY, baseZ;

    private int x, y, z;

    private int sizeX, sizeY, sizeZ;

    public SimpleIterator(World world,
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

    public Block next() {
        Block block = world.getBlockAt(baseX + x, baseY + y, baseZ + z);

        if (++x >= this.sizeX) {
            this.x = 0;
            if (++this.y >= this.sizeY) {
                this.y = 0;
                ++this.z;
            }
        }

        return block;
    }

}
