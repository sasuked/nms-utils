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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@AllArgsConstructor
public class SelectionListener implements Listener {

    private DefToolPlugin plugin;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if(!plugin.getNpcSelectionMap().containsKey(uniqueId)) return;
        if(plugin.getNpcSelectionMap().get(uniqueId) != null) return;

        Entity rightClicked = event.getRightClicked();
        if(rightClicked == null || rightClicked.getType() != EntityType.PLAYER) {
            return;
        }

        Npc npc = NpcRegistry.getInstance().getByEntityId(rightClicked.getEntityId());
        if(npc == null) return;

        plugin.getNpcSelectionMap().put(uniqueId, npc);
        player.sendMessage(" §a§l! §7NPC successfully selected.");

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getNpcSelectionMap().remove(player.getUniqueId());
    }

}
