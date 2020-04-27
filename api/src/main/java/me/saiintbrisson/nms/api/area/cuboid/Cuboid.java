package me.saiintbrisson.nms.api.area.cuboid;

import me.saiintbrisson.nms.api.area.BlockPosition;
import me.saiintbrisson.nms.api.area.SimpleBlockPosition;
import org.apache.commons.lang.ArrayUtils;

public interface Cuboid<B> extends Iterable<B>, Cloneable {

    int getLowerX();
    int getLowerY();
    int getLowerZ();

    int getUpperX();
    int getUpperY();
    int getUpperZ();

    default int getSizeX() {
        return (getUpperX() - getLowerX()) + 1;
    }
    default int getSizeY() {
        return (getUpperY() - getLowerY()) + 1;
    }
    default int getSizeZ() {
        return (getUpperZ() - getLowerZ()) + 1;
    }

    default int getVolume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }

    default BlockPosition getCenter() {
        return new SimpleBlockPosition(
            getLowerX() + ((getUpperX() + 1) - getLowerX()) / 2,
            getLowerY() + ((getUpperY() + 1) - getLowerY()) / 2,
            getLowerZ() + ((getUpperZ() + 1) - getLowerZ()) / 2
        );
    }

    default ChunkPosition[] getChunks() {
        ChunkPosition[] chunks = new ChunkPosition[0];

        for(int x = getLowerX(); x <= getUpperX(); x += 16) {
            for(int z = getLowerZ(); z <= getUpperZ(); z += 16) {
                chunks = (ChunkPosition[]) ArrayUtils.add(
                    chunks,
                    new ChunkPosition(
                        x >> 4,
                        z >> 4
                    )
                );
            }
        }

        return chunks;
    }

    B[] getBorder(int y);

    default PositionIterator positionIterator() {
        return new PositionIterator(
            getLowerX(), getLowerY(), getLowerZ(),
            getUpperX(), getUpperY(), getUpperZ()
        );
    }

}
