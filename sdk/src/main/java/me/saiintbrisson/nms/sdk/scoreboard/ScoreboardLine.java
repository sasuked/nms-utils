package me.saiintbrisson.nms.sdk.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public class ScoreboardLine {

    private String text;
    private Function<Player, String> function;

    public ScoreboardLine(String text) {
        this.text = text;
    }

    public ScoreboardLine(Function<Player, String> function) {
        this.function = function;
    }

    public boolean isUpdatable() {
        return function != null;
    }
    public boolean isUpdatable(String text) {
        if(this.text == null) return true;

        return !this.text.equals(text);
    }

    public String getText(Player player) {
        String result = null;
        if(function != null && player != null) {
            result = function.apply(player);
        }

        return result == null ? text : result;
    }

}
