package me.saiintbrisson.nms.test.action;

import lombok.AllArgsConstructor;
import me.saiintbrisson.nms.test.TestPlugin;
import me.saiintbrisson.nms.api.area.SimpleBlockPosition;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor
public class ActionListener implements Listener {

    private TestPlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();
        Block clickedBlock = event.getClickedBlock();

        String name = event.getAction().name();
        if(itemInHand == null
            || itemInHand.getType() != Material.WOOD_AXE
            || clickedBlock == null
            || !player.hasPermission("nms.edit")
            || player.getGameMode() != GameMode.CREATIVE
            || !name.endsWith("BLOCK")) {
            return;
        }

        UUID uniqueId = player.getUniqueId();
        Action action = plugin.getActions().get(uniqueId);

        if(action == null) {
            action = new Action(uniqueId);
            plugin.getActions().put(uniqueId, action);
        }

        Location location = clickedBlock.getLocation();
        SimpleBlockPosition position = new SimpleBlockPosition(
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ()
        );

        if(name.startsWith("LEFT")) {
            action.setLower(position);
            player.sendMessage(String.format(" §a§l! §7Point §f#1 §7defined to §f%s§7.", position.formatPosition()));
        } else {
            action.setUpper(position);
            player.sendMessage(String.format(" §a§l! §7Point §f#2 §7defined to §f%s§7.", position.formatPosition()));
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getActions().remove(player.getUniqueId());
    }

}
