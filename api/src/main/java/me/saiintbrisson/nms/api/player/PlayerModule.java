package me.saiintbrisson.nms.api.player;

import org.bukkit.entity.Player;

public interface PlayerModule {

    void sendMessage(Player player, ChatType type, String message);

}
