package me.saiintbrisson.nms.sdk.packet;

import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.sdk.entities.npc.NmsNpc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PacketRegister implements Listener {

    private static final PacketTracker TRACKER = new PacketTracker();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TRACKER.inject(player);

        for(Npc npc : NpcRegistry.getInstance().getAll()) {
            if(npc.isVisibleToAll()) {
                ((NmsNpc) npc).getVisibleMap().put(player.getUniqueId(), 60);
                npc.spawn(player);
            }
        }
    }

}
