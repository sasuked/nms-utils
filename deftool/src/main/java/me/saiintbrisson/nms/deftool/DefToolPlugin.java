package me.saiintbrisson.nms.deftool;

import lombok.Getter;
import me.saiintbrisson.commands.CommandFrame;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.deftool.action.Action;
import me.saiintbrisson.nms.deftool.commands.npc.CreationCommand;
import me.saiintbrisson.nms.deftool.commands.npc.SkinCommand;
import me.saiintbrisson.nms.deftool.listeners.ActionListener;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.area.AreaHistory;
import me.saiintbrisson.nms.api.entities.npc.SkinLayer;
import me.saiintbrisson.nms.deftool.commands.npc.NpcCommand;
import me.saiintbrisson.nms.deftool.commands.edit.EditCommand;
import me.saiintbrisson.nms.deftool.commands.edit.SetCommand;
import me.saiintbrisson.nms.deftool.commands.edit.UndoCommand;
import me.saiintbrisson.nms.deftool.listeners.npc.SelectionListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class DefToolPlugin extends JavaPlugin {

    private AreaHistory history;
    private Map<UUID, Action> actions = new HashMap<>();

    private Map<UUID, Npc> npcSelectionMap = new HashMap<>();

    @Override
    public void onEnable() {
        CommandFrame frame = new CommandFrame(this);

        frame.registerType(SkinLayer.class, argument -> SkinLayer.valueOf(argument.toUpperCase()));

        frame.register(
          new EditCommand(this),
          new SetCommand(this),
          new UndoCommand(this)
        );

        frame.register(
          new NpcCommand(this),
          new CreationCommand(this),
          new SkinCommand(this)
        );

        getServer().getPluginManager().registerEvents(new ActionListener(this), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(this), this);

        history = NmsAPI.getAreaHistory();
    }

}
