package me.saiintbrisson.nms.test;

import lombok.Getter;
import me.saiintbrisson.commands.CommandFrame;
import me.saiintbrisson.nms.test.action.Action;
import me.saiintbrisson.nms.test.action.ActionListener;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.area.AreaHistory;
import me.saiintbrisson.nms.api.entities.npc.SkinLayer;
import me.saiintbrisson.nms.test.commands.npc.NpcCommand;
import me.saiintbrisson.nms.test.commands.edit.EditCommand;
import me.saiintbrisson.nms.test.commands.edit.SetCommand;
import me.saiintbrisson.nms.test.commands.edit.UndoCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class TestPlugin extends JavaPlugin {

    private AreaHistory history;
    private Map<UUID, Action> actions = new HashMap<>();

    @Override
    public void onEnable() {
        CommandFrame frame = new CommandFrame(this);

        frame.registerType(SkinLayer.class, argument -> SkinLayer.valueOf(argument.toUpperCase()));

        frame.register(
          new EditCommand(this),
          new SetCommand(this),
          new UndoCommand(this),

          new NpcCommand()
        );

        getServer().getPluginManager().registerEvents(new ActionListener(this), this);

        history = NmsAPI.getAreaHistory();
    }

}
