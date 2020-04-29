package me.saiintbrisson.nms.api.entities.npc;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import me.saiintbrisson.nms.api.entities.MojangProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Data
@Builder
@ToString
public class PersistentNpc {

    private int id;
    private String name;

    private String worldName;
    private double x, y, z;
    private float yaw, pitch;

    private MojangProfile profile;
    private SkinLayer[] skinLayers;

    private boolean artificialIntelligence;
    private int targetRadius;

    public Location getLocation() {
        return new Location(
          Bukkit.getWorld(worldName),
          x, y, z, yaw, pitch
        );
    }

    public static PersistentNpc of(Npc npc) {
        Location location = npc.getLocation();
        return PersistentNpc.builder()
          .id(npc.getNpcId())
          .name(npc.getCompleteName())

          .worldName(location.getWorld().getName())
          .x(location.getX())
          .y(location.getY())
          .z(location.getZ())
          .yaw(location.getYaw())
          .pitch(location.getPitch())

          .profile(npc.getMojangProfile())
          .skinLayers(npc.getSkinLayers())

          .artificialIntelligence(npc.isArtificialIntelligence())
          .targetRadius(npc.getTargetRadius())
          .build();
    }

}
