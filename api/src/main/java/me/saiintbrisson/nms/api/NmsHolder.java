package me.saiintbrisson.nms.api;

import me.saiintbrisson.nms.api.area.AreaBlock;
import me.saiintbrisson.nms.api.area.AreaEditor;
import me.saiintbrisson.nms.api.area.AreaHistory;
import me.saiintbrisson.nms.api.area.AreaWorld;
import me.saiintbrisson.nms.api.area.cuboid.Cuboid;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.packet.PacketListener;
import me.saiintbrisson.nms.api.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public interface NmsHolder {

    Collection<PacketListener> getPacketListeners();
    void registerPacketListener(Plugin owner, PacketListener packetListener);

    AreaWorld<? extends AreaBlock> getNmsWorld(World world);

    default AreaWorld<? extends AreaBlock> getNmsWorld(String world) {
        return getNmsWorld(Bukkit.getWorld(world));
    }

    <B extends AreaBlock> Cuboid<B> getCuboid(AreaWorld<B> world,
                                              int lowerX, int lowerY, int lowerZ,
                                              int upperX, int upperY, int upperZ);

    <B extends AreaBlock> AreaEditor<B, ? extends AreaWorld<B>> getAreaEditor(AreaWorld<B> world);

    AreaHistory<?> getAreaHistory();

    void setAsyncCatcher(boolean status);

    Scoreboard createScoreboard(String name, String title);

    Npc createNpc(World world, String prefix, String name, String complement);

}
