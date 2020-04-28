package me.saiintbrisson.nms.deftool.commands.npc;

import lombok.AllArgsConstructor;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.argument.Argument;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.deftool.DefToolPlugin;

import java.util.UUID;

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
          " §f/npc create [prefix] <name> [complement] §8- §7Creates a NPC;",
          " §f/npc delete [id] §8- §7Deletes the selected NPC;",
          " §f/npc skin <name> [id] §8- §7Changes the skin of the selected NPC;",
          " §f/npc layers <array of layers> §8- §7Changes the visible skin layers of the selected NPC;",
          " §f/npc tp [id] §8- §7Teleports you to the selected NPC;",
          " §f/npc tphere [id] §8- §7Teleports the selected NPC to you;",
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

        if(plugin.getNpcSelections().containsKey(uniqueId)) {
            plugin.getNpcSelections().remove(uniqueId);
            execution.sendMessage(" §a§l! §7You've been removed from NPC selection.");
            return;
        }

        if(id != null) {
            Npc npc = NpcRegistry.getInstance().getByEntityId(id);
            if(npc == null) {
                execution.sendMessage("§cInvalid NPC id.");
                return;
            }

            plugin.getNpcSelections().put(uniqueId, npc);
            execution.sendMessage(" §a§l! §7Successfully selected NPC.");
            return;
        }

        execution.sendMessage(" §a§l! §7Click on the NPC you want to select.");
        execution.sendMessage(" §a§l! §7Type §f%s§7 to exit selection.", "/npc select");
    }

}
