package me.saiintbrisson.nms.api;

import lombok.Getter;
import me.saiintbrisson.nms.api.area.*;
import me.saiintbrisson.nms.api.area.cuboid.Cuboid;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.packet.PacketListener;
import me.saiintbrisson.nms.api.scoreboard.Scoreboard;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public final class NmsAPI {

    @Getter
    private static NmsHolder holder;

    public static final void setHolder(NmsHolder holder) {
        if(NmsAPI.holder != null) {
            throw new IllegalStateException("Cannot replace existing holder singleton");
        }

        NmsAPI.holder = holder;
    }

    public Collection<PacketListener> getPacketListeners() {
        return getHolder().getPacketListeners();
    }

    public static void registerPacketListener(Plugin owner, PacketListener packetListener) {
        getHolder().registerPacketListener(owner, packetListener);
    }

    public static AreaWorld<? extends AreaBlock> getNmsWorld(World world) {
        return getHolder().getNmsWorld(world);
    }

    public static AreaWorld<? extends AreaBlock> getNmsWorld(String world) {
        return getHolder().getNmsWorld(world);
    }

    public static <B extends AreaBlock> Cuboid<B> getCuboid(AreaWorld<B> world,
                                                            BlockPosition lower, BlockPosition upper) {
        return getHolder().getCuboid(
            world,
            lower.getX(), lower.getY(), lower.getZ(),
            upper.getX(), upper.getY(), upper.getZ()
        );
    }

    public static <B extends AreaBlock> Cuboid<B> getCuboid(AreaWorld<B> world,
                                              int lowerX, int lowerY, int lowerZ,
                                              int upperX, int upperY, int upperZ) {
        return getHolder().getCuboid(
            world,
            lowerX, lowerY, lowerZ,
            upperX, upperY, upperZ
        );
    }

    public static <B extends AreaBlock> AreaEditor<B, ? extends AreaWorld<B>> getAreaEditor(AreaWorld<B> world) {
        return getHolder().getAreaEditor(world);
    }

    public static AreaHistory<?> getAreaHistory() {
        return getHolder().getAreaHistory();
    }

    public static void setAsyncCatcher(boolean status) {
        getHolder().setAsyncCatcher(status);
    }

    public static Scoreboard createScoreboard(String name, String title) {
        return getHolder().createScoreboard(name, title);
    }

    public static Npc createNpc(World world, String prefix, String name, String complement) {
        return getHolder().createNpc(world, prefix, name, complement);
    }

}
