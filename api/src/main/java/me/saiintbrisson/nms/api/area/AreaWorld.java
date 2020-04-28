package me.saiintbrisson.nms.api.area;

import me.saiintbrisson.nms.api.area.cuboid.Cuboid;
import org.bukkit.Location;

public interface AreaWorld<B extends BlockPosition> {

    B getBlock(int x, int y, int z);

    default B getBlock(BlockPosition position) {
        return getBlock(
            position.getX(),
            position.getY(),
            position.getZ()
        );
    }

    default B getBlock(Location location) {
        return getBlock(
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ()
        );
    }

    Cuboid<B> getRegion(int lowerX, int lowerY, int lowerZ, int upperX, int upperY, int upperZ);

    default Cuboid<B> getRegion(Location lowerLocation, Location upperLocation) {
        return getRegion(
            lowerLocation.getBlockX(),
            lowerLocation.getBlockY(),
            lowerLocation.getBlockZ(),
            upperLocation.getBlockX(),
            upperLocation.getBlockY(),
            upperLocation.getBlockZ()
        );
    }
    
    default Cuboid<B> getRegion(BlockPosition lowerPosition, BlockPosition upperPosition) {
        return getRegion(
            lowerPosition.getX(),
            lowerPosition.getY(),
            lowerPosition.getZ(),
            upperPosition.getX(),
            upperPosition.getY(),
            upperPosition.getZ()
        );
    }

    void setBlock(int x, int y, int z, int id, byte data);

    default void setBlock(BlockPosition position, int id, byte data) {
        setBlock(
            position.getX(),
            position.getY(),
            position.getZ(),
            id,
            data
        );
    }

    default void setBlock(Location location, int id, byte data) {
        setBlock(
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ(),
            id,
            data
        );
    }


}
