package me.saiintbrisson.nms.sdk.scoreboard;

import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class NmsTeam extends ScoreboardTeam {

    private String start, middle = "", end = "";

    public NmsTeam(Scoreboard scoreboard, String name, String prefix, String playerName, String complement) {
        super(scoreboard, name);

        start = prefix == null ? "" : prefix;
        middle = playerName == null ? "" : playerName;
        end = complement == null ? "" : complement;
    }

    public NmsTeam(Scoreboard scoreboard, String name, String text) {
        super(scoreboard, name);

        if(text.length() >= 16) {
            start = text.substring(0, 16);

            if(text.length() >= 32) {
                middle = text.substring(16, 30);
                if(start.endsWith("§")) {
                    start = start.substring(0, start.length() - 1);
                    middle = "§" + middle;
                } else if(!middle.startsWith("§")) {
                    char lastColor = getLastColor(start);
                    if(lastColor != 0) middle = "§" + lastColor + middle;
                }

                end = text.substring(30);
                if(middle.endsWith("§")) {
                    middle = middle.substring(0, middle.length() - 1);
                    end = "§" + end;
                } else if(!end.startsWith("§")) {
                    char lastColor = getLastColor(middle);
                    if(lastColor != 0) end = "§" + lastColor + end;
                }
            } else {
                middle = text.substring(16);
            }
        } else {
            start = text;
        }
    }

    public NmsTeam(Scoreboard scoreboard, int slot, ScoreboardLine line, Player player) {
        super(scoreboard, "line-" + slot);

        String text = line.getText();
        Function<Player, String> function = line.getFunction();
        if(text != null && function != null) {
            if(text.length() > 30) {
                throw new IllegalArgumentException("Static text is longer than 30 characters");
            }

            if(text.length() > 16) {
                start = text.substring(0, 16);
                middle = text.substring(16);

                if(!middle.startsWith("§")) {
                    if(start.endsWith("§")) {
                        start = start.substring(0, start.length() - 1);
                        middle = "§" + middle;
                    } else {
                        char lastColor = getLastColor(start);
                        if(lastColor != 0) middle = "§" + lastColor + middle;
                    }
                }
            } else {
                start = text;
            }

            end = function.apply(player);
            if(end.length() > 16) {
                throw new IllegalArgumentException("Dynamic text is longer than 16 characters");
            }
        } else if(text != null) {
            if(text.length() >= 16) {
                start = text.substring(0, 16);

                if(text.length() >= 32) {
                    middle = text.substring(16, 30);
                    if(start.endsWith("§")) {
                        start = start.substring(0, start.length() - 1);
                        middle = "§" + middle;
                    } else if(!middle.startsWith("§")) {
                        char lastColor = getLastColor(start);
                        if(lastColor != 0) middle = "§" + lastColor + middle;
                    }

                    end = text.substring(30);
                    if(middle.endsWith("§")) {
                        middle = middle.substring(0, middle.length() - 1);
                        end = "§" + end;
                    } else if(!end.startsWith("§")) {
                        char lastColor = getLastColor(middle);
                        if(lastColor != 0) end = "§" + lastColor + end;
                    }
                } else {
                    middle = text.substring(16);
                }
            } else {
                start = text;
            }
        } else if(function != null) {
            if(text.length() >= 16) {
                start = text.substring(0, 16);

                if(text.length() > 30) {
                    end = text.substring(16, 30);
                } else {
                    end = text.substring(16);
                }

                if(start.endsWith("§")) {
                    start = start.substring(0, start.length() - 1);
                    end = "§" + end;
                } else if(!end.startsWith("§")) {
                    char lastColor = getLastColor(start);
                    if(lastColor != 0) end = "§" + lastColor + end;
                }
            } else {
                start = text;
            }
        }

        if(middle.equals("")) {
            middle = ChatColor.values()[slot].toString() + ChatColor.RESET.toString();
        }
    }

    private char getLastColor(String before) {
        for(int i = before.length() - 1; i > 0; i--) {
            char c = before.charAt(i);
            if(c == '§') {
                return before.charAt(i + 1);
            }
        }

        return 0;
    }

    @Override
    public String getPrefix() {
        return start;
    }

    @Override
    public String getSuffix() {
        return end;
    }

    public String getMiddle() {
        return middle;
    }

    @Override
    public Collection<String> getPlayerNameSet() {
        return Collections.singletonList(middle);
    }

    @Override
    public EnumNameTagVisibility getNameTagVisibility() {
        return EnumNameTagVisibility.ALWAYS;
    }

}
