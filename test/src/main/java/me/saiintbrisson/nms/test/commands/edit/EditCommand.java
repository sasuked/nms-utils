package me.saiintbrisson.nms.test.commands.edit;

import lombok.AllArgsConstructor;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.nms.test.TestPlugin;
import me.saiintbrisson.nms.test.action.Action;
import me.saiintbrisson.nms.api.area.SimpleBlockPosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
public class EditCommand {

    private TestPlugin plugin;

    @Command(
        name = "edit",
        permission = "nms.edit",
        inGameOnly = true
    )
    public void edit(Execution execution) {
        execution.sendMessage(new String[]{
            " ",
            " §a§lEDIT - HELP",
            " ",
            " §f/edit §8- §7Shows this message;",
            " ",
            " §f/edit set <id> [data] §8- §7Sets a cuboid with id and data;",
            " §f/edit undo §8- §7Undoes the latest change;",
            " ",
            " §f/edit pos1 §8- §7Sets the edit pos1 to your current position;",
            " §f/edit pos2 §8- §7Sets the edit pos2 to your current position;",
            " "
        });
    }

    @Command(
        name = "edit.pos1"
    )
    public void editPos1(Execution execution) {
        Player player = execution.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Action action = plugin.getActions().get(uniqueId);

        if(action == null) {
            action = new Action(uniqueId);
            plugin.getActions().put(uniqueId, action);
        }

        Location location = player.getLocation();
        SimpleBlockPosition position = new SimpleBlockPosition(
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ()
        );

        action.setLower(position);

        player.sendMessage(String.format(" §a§l! §7Point §f#1 §7defined to §f%s§7.", position.formatPosition()));
    }

    @Command(
        name = "edit.pos2"
    )
    public void editPos2(Execution execution) {
        Player player = execution.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Action action = plugin.getActions().get(uniqueId);

        if(action == null) {
            action = new Action(uniqueId);
            plugin.getActions().put(uniqueId, action);
        }

        Location location = player.getLocation();
        SimpleBlockPosition position = new SimpleBlockPosition(
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ()
        );

        action.setUpper(position);

        player.sendMessage(String.format(" §a§l! §7Point §f#2 §7defined to §f%s§7.", position.formatPosition()));
    }

}
