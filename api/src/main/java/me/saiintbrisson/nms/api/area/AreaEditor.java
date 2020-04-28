package me.saiintbrisson.nms.api.area;

import me.saiintbrisson.nms.api.area.cuboid.Cuboid;

public interface AreaEditor<B extends BlockPosition, W extends AreaWorld<B>> {

    void setIgnoreHistory(boolean ignoreHistory);
    void setHistory(AreaHistory<?> history);

    int getChangesSize();
    int getCommitChanges();
    long getCommitPeriod();

    default void setBlock(BlockPosition position, int id) {
        setBlock(position, id, (byte) 0);
    }
    default void setBlock(int x, int y, int z, int id) {
        setBlock(x, y, z, id, (byte) 0);
    }
    default void setBlock(int x, int y, int z, int id, byte data) {
        setBlock(new SimpleBlockPosition(x, y, z), id, data);
    }

    void setBlock(BlockPosition position, int id, byte data);

    default void setBlocks(Cuboid<B> cuboid, int id) {
        setBlocks(cuboid, id, (byte) 0);
    }

    void setBlocks(Cuboid<B> cuboid, int id, byte data);

    void commit();
    boolean undo();

}
