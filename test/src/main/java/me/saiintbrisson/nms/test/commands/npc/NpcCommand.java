package me.saiintbrisson.nms.test.commands.npc;

import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.entities.MojangProperty;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.entities.npc.SkinLayer;
import me.saiintbrisson.nms.api.util.SkinFetcher;
import org.bukkit.entity.Player;

public class NpcCommand {

    @Command(
      name = "npc",
      inGameOnly = true
    )
    public void npc(Execution execution, String skinName, boolean legacy) {
        Player player = execution.getPlayer();
        Npc npc = NmsAPI.createNpc(player.getWorld(), "§8[NPC] §6", skinName, "'s §fskin");

        npc.setLocation(player.getLocation());

        MojangProperty skin = SkinFetcher.fetchSkinFromName(skinName, legacy);
        if(skin != null) {
            npc.setProperty(skin);
        }

        npc.setArtificialIntelligence(true);
        npc.setInvincible(true);

        npc.setVisibleToAll(true);
        npc.setSkinLayers(SkinLayer.ALL);

        npc.trackEntity();
    }

}
