package me.saiintbrisson.nms.sdk.scoreboard;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;

public class NmsScoreboardObjective extends ScoreboardObjective {

    private String name;

    public NmsScoreboardObjective(Scoreboard scoreboard, String name, String title) {
        super(scoreboard, title, IScoreboardCriteria.b);

        this.name = name.length() > 16 ? name.substring(0, 16) : name;
    }

    @Override
    public String getName() {
        return name;
    }

}
