package me.saiintbrisson.nms.api.hologram;

import org.bukkit.entity.Player;

import java.util.List;

public interface Hologram {

    double getX();
    double getY();
    double getZ();

    List<String> getLines();

    void move(byte relX, byte relY, byte relZ);
    void teleport(int x, int y, int z);

    void addLine(String line, boolean onTop);
    default void addLine(String line) {
        addLine(line, false);
    }

    void render(Player player);

}
