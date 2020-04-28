package me.saiintbrisson.nms.test.commands.edit;

import lombok.AllArgsConstructor;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.nms.test.TestPlugin;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.area.AreaEditor;
import me.saiintbrisson.nms.api.area.AreaWorld;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class UndoCommand {

    private TestPlugin plugin;

    @Command(
        name = "edit.undo",

        options = "async"
    )
    public void undo(Execution execution) {
        Player player = execution.getPlayer();

        AreaWorld world = NmsAPI.getNmsWorld(player.getWorld());
        AreaEditor editor = NmsAPI.getAreaEditor(world);
        editor.setHistory(plugin.getHistory());
        editor.setIgnoreHistory(true);

        if(!editor.undo()) {
            execution.sendMessage(" §c§l! §7Nothing to undo.");
            return;
        }

        if(execution.hasOption("async")) {
            CompletableFuture.runAsync(() -> process(execution, editor, true));
        } else {
            process(execution, editor, false);
        }
    }

    private void process(Execution execution,
                         AreaEditor editor, boolean async) {
        execution.sendMessage(" §a§l! §7Undoing §f%s§7 blocks.", editor.getChangesSize());
        editor.commit();
        execution.sendMessage(
            " §a§l! §7Done in §f%sms§7 (§f%s§7 blocks%s§7).",
            editor.getCommitPeriod(),
            editor.getCommitChanges(),
            async ? ", §fasync" : ""
        );
    }

}
