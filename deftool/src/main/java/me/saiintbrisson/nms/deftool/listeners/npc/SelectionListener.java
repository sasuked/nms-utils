package me.saiintbrisson.nms.deftool.listeners.npc;

import lombok.AllArgsConstructor;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.deftool.DefToolPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@AllArgsConstructor
public class SelectionListener implements Listener {

    private DefToolPlugin plugin;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        /*if(!)

        Entity rightClicked = event.getRightClicked();
        if(rightClicked == null || rightClicked.getType() != EntityType.PLAYER) {
            return;
        }

        Npc byEntityId = NpcRegistry.getInstance().getByEntityId(rightClicked.getEntityId());*/
    }

}
