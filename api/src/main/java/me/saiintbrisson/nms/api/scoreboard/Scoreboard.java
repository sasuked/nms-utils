package me.saiintbrisson.nms.api.scoreboard;

import org.bukkit.entity.Player;

import java.util.function.Function;

public interface Scoreboard {

    void setTitle(String title);
    void setUpdateDelay(long updateDelay);

    void setLine(int slot, String line);
    void setLine(int slot, Function<Player, String> line);
    void setLine(int slot, String line, Function<Player, String> functionLine);

    void blankLine(int slot);

    void show(Player player);

    void update();
    void update(Player player);
    void update(Player player, int slot);

    void remove(Player player);

}
