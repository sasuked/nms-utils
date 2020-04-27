package me.saiintbrisson.nms.api.area;

public interface BlockPosition {

    int getX();
    int getY();
    int getZ();

    default String formatPosition() {
        return String.format("%s, %s, %s", getX(), getY(), getZ());
    }

}
