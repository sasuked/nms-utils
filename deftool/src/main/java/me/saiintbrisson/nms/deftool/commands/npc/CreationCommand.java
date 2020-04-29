package me.saiintbrisson.nms.deftool.commands.npc;

import lombok.AllArgsConstructor;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.argument.Argument;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.entities.npc.PlayerInfoAction;
import me.saiintbrisson.nms.deftool.DefToolPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
public class CreationCommand {

    private DefToolPlugin plugin;

    @Command(
      name = "npc.create",
      usage = "npc create <name, up to 44 characters> [--ai, --damageable]",

      options = { "-ai", "-damageable" }
    )
    public void create(Execution execution, String... nameArray) {
        String name = String.join(" ", nameArray);

        if(name.length() > 44)  {
            execution.sendMessage(" §c§l! §7The name should not be longer than 44 characters.");
            return;
        }

        Player player = execution.getPlayer();
        Location location = player.getLocation();
        Npc npc = NmsAPI.createNpc(location.getWorld(), name);

        npc.setLocation(location);

        if(execution.hasOption("-ai")) {
            npc.setArtificialIntelligence(true);
        }

        if(!execution.hasOption("-damageable")) {
            npc.setInvincible(true);
        }

        npc.setVisibleToAll(true);

        npc.trackEntity();
        Iterable<Player> onlinePlayers = (Iterable<Player>) Bukkit.getOnlinePlayers();
        npc.setTabListTimeout(onlinePlayers, 60);
        npc.spawn(onlinePlayers);

        NmsAPI.saveAllNpcs();
        plugin.getNpcSelectionMap().put(player.getUniqueId(), npc);
        execution.sendMessage(" §a§l! §7Successfully created NPC.");
    }

    @Command(
      name = "npc.delete",
      aliases = "remove",

      usage = "npc delete [id]"
    )
    public void delete(Execution execution) {
        UUID uniqueId = execution.getPlayer().getUniqueId();

        if(!plugin.getNpcSelectionMap().containsKey(uniqueId)) {
            execution.sendMessage(" §c§l! §7Make sure to select a NPC first.");
            return;
        }

        Npc npc = plugin.getNpcSelectionMap().get(uniqueId);
        if(npc == null) {
            execution.sendMessage(" §c§l! §7Make sure to select a NPC first.");
            return;
        }

        Iterable<Player> onlinePlayers = (Iterable<Player>) Bukkit.getOnlinePlayers();

        npc.stopTracking();
        npc.destroy(onlinePlayers);
        npc.dispatchPlayerInfo(onlinePlayers, PlayerInfoAction.REMOVE_PLAYER);

        NmsAPI.saveAllNpcs();
        plugin.getNpcSelectionMap().remove(uniqueId);
        execution.sendMessage(" §a§l! §7Successfully deleted NPC.");
    }

}
