package me.saiintbrisson.nms.deftool.commands.edit;

import lombok.AllArgsConstructor;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.nms.deftool.DefToolPlugin;
import me.saiintbrisson.nms.deftool.action.Action;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.area.AreaEditor;
import me.saiintbrisson.nms.api.area.AreaWorld;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class SetCommand {

    private DefToolPlugin plugin;

    @Command(
        name = "edit.set",
        usage = "edit set <id> [data]",

        options = "async"
    )
    public void set(Execution execution, int id) {
        Player player = execution.getPlayer();
        Action action = plugin.getActions().get(player.getUniqueId());

        if(action == null || !action.isComplete()) {
            execution.sendMessage("§cYou must select point 1 and 2 before making and edit.");
            return;
        }

        byte data = 0;
        if(execution.argsCount() == 2) {
            try {

                data = Byte.parseByte(execution.getArg(1));
                if(data < 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {
                execution.sendMessage("§cData must be between 0 and 127");
                return;
            }
        }

        AreaWorld world = NmsAPI.getNmsWorld(player.getWorld());
        AreaEditor editor = NmsAPI.getAreaEditor(world);
        editor.setHistory(plugin.getHistory());

        if(execution.hasOption("async")) {
            byte finalData = data;
            CompletableFuture.runAsync(() -> process(execution, action, id, finalData, editor, world, true));
        } else {
            process(execution, action, id, data, editor, world, false);
        }
    }

    private void process(Execution execution, Action action,
                         int id, byte data,
                         AreaEditor editor, AreaWorld world, boolean async) {
        editor.setBlocks(
            NmsAPI.getCuboid(world, action.getLower(), action.getUpper()),
            id, data
        );

        execution.sendMessage(" §a§l! §7Editing §f%s§7 blocks.", editor.getChangesSize());
        editor.commit();
        execution.sendMessage(
            " §a§l! §7Done in §f%sms§7 (§f%s§7 blocks%s§7).",
            editor.getCommitPeriod(),
            editor.getCommitChanges(),
            async ? ", §fasync" : ""
        );
    }

}
