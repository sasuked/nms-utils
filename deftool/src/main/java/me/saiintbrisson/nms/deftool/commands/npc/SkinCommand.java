package me.saiintbrisson.nms.deftool.commands.npc;

import lombok.AllArgsConstructor;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.annotations.Completer;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.entities.MojangProperty;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.entities.npc.PlayerInfoAction;
import me.saiintbrisson.nms.api.entities.npc.SkinLayer;
import me.saiintbrisson.nms.api.util.SkinFetcher;
import me.saiintbrisson.nms.deftool.DefToolPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SkinCommand {

    private DefToolPlugin plugin;

    @Command(
      name = "npc.skin",
      usage = "npc skin <name> [--mineskin]",

      options = "-mineskin"
    )
    public void skin(Execution execution, String skinName) {
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

        MojangProperty skin = SkinFetcher.fetchSkinFromName(skinName, !execution.hasOption("-mineskin"));
        if(skin == null) {
            execution.sendMessage(" §c§l! §7Could not retrieve skin from servers.");
            return;
        }

        npc.setProperty(skin);
        npc.destroy(onlinePlayers);
        npc.dispatchPlayerInfo(onlinePlayers, PlayerInfoAction.REMOVE_PLAYER);
        npc.setTabListTimeout(onlinePlayers, 60);
        npc.spawn(onlinePlayers);

        NmsAPI.saveAllNpcs();
        execution.sendMessage(" §a§l! §7Successfully changed NPC skin.");
    }

    @Command(
      name = "npc.layers",
      usage = "npc layers <array of layers>"
    )
    public void layers(Execution execution, SkinLayer[] layers) {
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

        npc.setSkinLayers(layers);
        npc.updateMetadata((Iterable<Player>) Bukkit.getOnlinePlayers());

        NmsAPI.saveAllNpcs();
        execution.sendMessage(" §a§l! §7Successfully changed NPC skin layers.");
    }

    @Completer(
      name = "npc.layers"
    )
    public List<String> completeLayers(Execution execution) {
        if(execution.argsCount() == 0) {
            return Arrays.stream(SkinLayer.values()).map(Enum::name).collect(Collectors.toList());
        }

        return Arrays.stream(SkinLayer.values())
          .map(Enum::name)
          .filter(it ->
            it.startsWith(execution.getArg(execution.argsCount() - 1).toUpperCase())
          )
          .collect(Collectors.toList());
    }

}
