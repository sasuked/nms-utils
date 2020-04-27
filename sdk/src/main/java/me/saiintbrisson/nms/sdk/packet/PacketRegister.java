package me.saiintbrisson.nms.sdk.packet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PacketRegister implements Listener {

    private static final PacketTracker TRACKER = new PacketTracker();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        TRACKER.inject(event.getPlayer());
    }

}
