package me.saiintbrisson.nms.api.area.cuboid;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Getter
public class SimpleCuboid implements Cuboid<Block> {

    private World world;

    private int lowerX, lowerY, lowerZ,
                upperX, upperY, upperZ;

    @Override
    public Block[] getBorder(int y) {
        Set<Block> blocks = new HashSet<>();

        for(int i = lowerX; i < upperX; i++) {
            blocks.add(world.getBlockAt(i, y, lowerZ));
            blocks.add(world.getBlockAt(i, y, upperZ));
        }

        for(int i = lowerZ; i < upperZ; i++) {
            blocks.add(world.getBlockAt(i, y, lowerX));
            blocks.add(world.getBlockAt(i, y, upperX));
        }

        return blocks.toArray(new Block[0]);
    }

    @Override
    public Iterator<Block> iterator() {
        return new SimpleIterator(
            world,
            lowerX, lowerY, lowerZ,
            upperX, upperY, upperZ
        );
    }

}
