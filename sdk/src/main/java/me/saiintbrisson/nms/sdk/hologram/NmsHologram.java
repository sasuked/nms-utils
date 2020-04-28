package me.saiintbrisson.nms.sdk.hologram;

import lombok.Getter;
import me.saiintbrisson.nms.api.hologram.Hologram;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class NmsHologram implements Hologram {

    private double x, y, z;
    private List<String> lines;

    private List<Player> tracking;
    private List<EntityArmorStand> stands;

    @Override
    public void move(byte relX, byte relY, byte relZ) {
        for(EntityArmorStand stand : stands) {
            PacketPlayOutEntity.PacketPlayOutRelEntityMove packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
              stand.getId(),
              relX,
              relY,
              relZ,
              false
            );

            for(Player player : tracking) {
                
            }
        }
    }

    @Override
    public void teleport(int x, int y, int z) {

    }

    @Override
    public void addLine(String line, boolean onTop) {

    }

    @Override
    public void render(Player player) {

    }

}
