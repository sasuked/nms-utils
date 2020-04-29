package me.saiintbrisson.nms.deftool.commands.npc;

import lombok.AllArgsConstructor;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.argument.Argument;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.deftool.DefToolPlugin;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NpcCommand {

    private DefToolPlugin plugin;

    @Command(
      name = "npc",
      inGameOnly = true,

      permission = "nms.npc"
    )
    public void npc(Execution execution) {
        execution.sendMessage(new String[]{
          " ",
          " §a§lNPC - HELP",
          " ",
          " §f/npc §8- §7Shows this message;",
          " ",
          " §f/npc select [id] §8- §7Lets you select a NPC by clicking on it;",
          " ",
          " §f/npc create <name, up to 44 characters> [--ai, --damageable] §8- §7Creates a NPC;",
          " §f/npc delete §8- §7Deletes the selected NPC;",
          " §f/npc skin <name> [--mineskin] §8- §7Changes the skin of the selected NPC;",
          " §f/npc layers <array of layers> §8- §7Changes the visible skin layers of the selected NPC;",
          " §f/npc tp [id] §8- §7Teleports you to the selected NPC;",
          " §f/npc tphere §8- §7Teleports the selected NPC to you;",
          " §f/npc info [id] §8- §7Shows you the info of the selected NPC;",
          " ",
          " §f/npc list §8- §7Lists all command-created NPCs;",
          " "
        });
    }

    @Command(
      name = "npc.select",
      usage = "npc select [id]"
    )
    public void npcSelect(Execution execution, @Argument(nullable = true) Integer id) {
        UUID uniqueId = execution.getPlayer().getUniqueId();

        if(plugin.getNpcSelectionMap().containsKey(uniqueId)) {
            plugin.getNpcSelectionMap().remove(uniqueId);
            if(id == null) {
                execution.sendMessage(" §a§l! §7You've been removed from NPC selection.");
                return;
            }
        }

        if(id != null) {
            Npc npc = NpcRegistry.getInstance().getByNpcId(id);
            if(npc == null) {
                execution.sendMessage(" §c§l! §7Invalid NPC id.");
                return;
            }

            plugin.getNpcSelectionMap().put(uniqueId, npc);
            execution.sendMessage(" §a§l! §7Successfully selected NPC.");
            return;
        }

        plugin.getNpcSelectionMap().put(uniqueId, null);
        execution.sendMessage(" §a§l! §7Click on the NPC you want to select.");
        execution.sendMessage(" §a§l! §7Type §f%s§7 to exit selection.", "/npc select");
    }

    @Command(
      name = "npc.list",
      usage = "npc list"
    )
    public void npcList(Execution execution) {
        Collection<Npc> all = NpcRegistry.getInstance()
          .getAll()
          .stream()
          .sorted(Comparator.comparingInt(Npc::getNpcId))
          .collect(Collectors.toList());

        execution.sendMessage(new String[]{
          " ",
          " §a§lNPC - LIST §7(§f" + all.size() + "§7)",
          " "
        });

        Npc selected = plugin.getNpcSelectionMap().get(execution.getPlayer().getUniqueId());
        for(Npc npc : all) {
            Location location = npc.getLocation();


            execution.sendMessage(
              (
                selected != null && selected.getNpcId() == npc.getNpcId()
                  ? " §7[§a" + npc.getNpcId() + "§7] §f"
                  : " §7[§f" + npc.getNpcId() + "§7] §f"
              )
                + npc.getName() + " §8- §7World: §f%s§7, X: §f%s§7, Y: §f%s§7, Z: §f%s§7;",

              location.getWorld().getName(),
              location.getBlockX(),
              location.getBlockY(),
              location.getBlockZ()
            );
        }

        if(all.size() == 0) {
            execution.sendMessage(" §cEmpty list.");
        }

        execution.sendMessage(" ");
    }

}
