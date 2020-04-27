package me.saiintbrisson.nms.sdk.npc;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.nms.api.entities.MojangProfile;
import me.saiintbrisson.nms.api.entities.MojangProperty;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.entities.npc.PlayerInfoAction;
import me.saiintbrisson.nms.api.entities.npc.SkinLayer;
import me.saiintbrisson.nms.sdk.npc.controllers.ConnectionController;
import me.saiintbrisson.nms.sdk.npc.controllers.PacketController;
import me.saiintbrisson.nms.sdk.scoreboard.NmsTeam;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.TrigMath;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.spigotmc.AsyncCatcher;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class NmsNpc extends EntityPlayer implements Npc {

    private final PacketController packetController;
    private final ConnectionController connectionController;

    private final Map<UUID, Boolean> visibleMap;

    private final PacketPlayOutScoreboardTeam teamPacket;
    private final PacketPlayOutScoreboardTeam destroyTeamPacket;

    private final PacketPlayOutPlayerInfo removalPacket;

    @Setter
    private boolean invincible;

    @Setter
    private boolean artificialIntelligence;

    @Setter
    private int targetRadius = 4;

    public NmsNpc(World world, String prefix, String name, String complement) {
        super(
          MinecraftServer.getServer(),
          ((CraftWorld) world).getHandle(),
          new GameProfile(UUID.nameUUIDFromBytes(("NPC=" + name).getBytes(StandardCharsets.UTF_8)), name),
          new PlayerInteractManager(((CraftWorld) world).getHandle())
        );

        packetController = new PacketController(this);
        connectionController = new ConnectionController(this);

        visibleMap = new ConcurrentHashMap<>();

        NmsTeam nmsTeam = new NmsTeam(
          null,
          "zz" + (name.length() > 14 ? name.substring(0, 14) : name),
          prefix,
          name,
          complement
        );

        teamPacket = new PacketPlayOutScoreboardTeam(nmsTeam, 0);
        destroyTeamPacket = new PacketPlayOutScoreboardTeam(nmsTeam, 1);

        removalPacket = new PacketPlayOutPlayerInfo(
          PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
          this
        );
    }

    @Override
    public Set<UUID> getVisibleTo() {
        return visibleMap.keySet();
    }

    @Override
    public MojangProfile getMojangProfile() {
        MojangProfile mojangProfile = new MojangProfile(getUniqueId(), getName());

        for(Map.Entry<String, Property> entry : getProfile().getProperties().entries()) {
            Property value = entry.getValue();
            mojangProfile.getPropertyMap().put(
              entry.getKey(),
              new MojangProperty(value.getName(), value.getValue(), value.getSignature())
            );
        }

        return mojangProfile;
    }

    @Override
    public UUID getUniqueId() {
        return getProfile().getId();
    }

    @Override
    public String getName() {
        return getProfile().getName();
    }

    @Override
    public Location getLocation() {
        return new Location(world.getWorld(), locX, locY, locZ, aI, pitch);
    }

    @Override
    public void setLocation(Location location) {
        if(location.getWorld() != null) {
            world = ((CraftWorld) location.getWorld()).getHandle();
        }

        locX = location.getX();
        locY = location.getY();
        locZ = location.getZ();

        aI = location.getYaw();
        pitch = location.getPitch();
    }

    @Override
    public void setSkinLayers(SkinLayer... layers) {
        byte base = 0x00;

        for(SkinLayer layer : layers) {
            base |= layer.getFlag();
        }

        datawatcher.watch(10, base);
    }

    @Override
    public void setProperty(String name, String value, String signature) {
        getProfile().getProperties().put(name, new Property(name, value, signature));
    }

    @Override
    public void trackEntity() {
        WorldServer worldServer = (WorldServer) this.world;
        AsyncCatcher.enabled = false;
        worldServer.b(Collections.singletonList(this));

        NpcRegistry.getInstance().register(this);
    }

    @Override
    public void lookAtEntity(LivingEntity entity) {
        float[] floats = calculateAngle(entity);
        aI = floats[0];
        pitch = floats[1];
    }

    private float[] calculateAngle(LivingEntity entity) {
        CraftLivingEntity livingEntity = ((CraftLivingEntity) entity);

        double x = entity.getLocation().getX();
        double y = entity.getLocation().getY();
        double f = y + livingEntity.getHandle().getHeadHeight();

        double d0 = x - locX;
        double d1 = f - (locY + 1.62);
        double d2 = entity.getLocation().getZ() - locZ;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        float ff = (float) (TrigMath.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
        float ff1 = (float) (-(TrigMath.atan2(d1, d3) * 180.0D / Math.PI));

        return new float[]{
          calculateAngle(aI, ff, 20),
          calculateAngle(pitch, ff1, 40)
        };
    }

    private float calculateAngle(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);

        if(f3 > f2) {
            f3 = f2;
        }

        if(f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public void dispatchPlayerInfo(Iterable<Player> players, PlayerInfoAction action) {
        packetController.dispatchPlayerInfo(players, action);
    }

    public void dispatchPlayerInfo(Player player, PlayerInfoAction action) {
        packetController.dispatchPlayerInfo(player, action);
    }

    public void spawn(Iterable<Player> players) {
        for(Player player : players) {
            visibleMap.put(player.getUniqueId(), true);
        }
        packetController.spawn(players);
    }

    public void spawn(Player player) {
        visibleMap.put(player.getUniqueId(), true);
        packetController.spawn(player);
    }

    public void destroy(Iterable<Player> players) {
        for(Player player : players) {
            visibleMap.remove(player.getUniqueId());
        }
        packetController.destroy(players);
        packetController.dispatchPlayerInfo(players, PlayerInfoAction.REMOVE_PLAYER);
    }

    public void destroy(Player player) {
        visibleMap.remove(player.getUniqueId());
        packetController.destroy(player);
        packetController.dispatchPlayerInfo(player, PlayerInfoAction.REMOVE_PLAYER);
    }

    public void updateMetadata(Iterable<Player> players) {
        packetController.updateMetadata(players);
    }

    public void updateMetadata(Player player) {
        packetController.updateMetadata(player);
    }

    public void updateHeadRotation(Iterable<Player> players) {
        packetController.updateHeadRotation(players);
    }

    public void updateHeadRotation(Player player) {
        packetController.updateHeadRotation(player);
    }

    public void updateHeadRotation(Iterable<Player> players, float yaw, float pitch) {
        packetController.updateHeadRotation(players, yaw, pitch);
    }

    public void updateHeadRotation(Player player, float yaw, float pitch) {
        packetController.updateHeadRotation(player, yaw, pitch);
    }

    @Override
    public void tick() {
        if(!artificialIntelligence) return;

        for(Map.Entry<UUID, Boolean> entry : getVisibleMap().entrySet()) {
            UUID key = entry.getKey();
            Player player = Bukkit.getPlayer(key);

            if(player == null) {
                visibleMap.remove(key);
                continue;
            }

            if(entry.getValue()) {
                visibleMap.put(player.getUniqueId(), false);
                continue;
            }

            packetController.sendPackets(player, removalPacket);
        }

        List<Player> toUpdate = Lists.newArrayList();
        boolean foundTarget = false;

        CraftWorld world = this.world.getWorld();
        for(Entity nearbyEntity : world.getNearbyEntities(getLocation(), targetRadius, targetRadius, targetRadius)) {
            if(nearbyEntity.getType() != EntityType.PLAYER || !(nearbyEntity instanceof LivingEntity)) {
                continue;
            }

            Player player = (Player) nearbyEntity;

            if(!visibleMap.containsKey(nearbyEntity.getUniqueId())) continue;

            if(!foundTarget) {
                lookAtEntity(player);
                foundTarget = true;
            }

            toUpdate.add(player);
        }

        updateHeadRotation(toUpdate);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if(invincible) return false;

        return super.damageEntity(damagesource, f);
    }

}
