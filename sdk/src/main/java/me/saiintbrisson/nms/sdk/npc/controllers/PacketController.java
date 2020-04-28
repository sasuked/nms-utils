package me.saiintbrisson.nms.sdk.npc.controllers;

import me.saiintbrisson.nms.api.entities.npc.PlayerInfoAction;
import me.saiintbrisson.nms.sdk.npc.NmsNpc;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketController {

    private NmsNpc parent;
    private final PacketPlayOutEntityDestroy destroyPacket;

    public PacketController(NmsNpc parent) {
        this.parent = parent;
        destroyPacket = new PacketPlayOutEntityDestroy(parent.getId());
    }

    public void spawn(Iterable<Player> players) {
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(parent);
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
          parent, (byte) MathHelper.d(parent.aI * 256.0F / 360.0F)
        );

        broadcastPackets(players, spawnPacket, headRotationPacket);
    }

    public void spawn(Player player) {
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(parent);
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
          parent, (byte) MathHelper.d(parent.aI * 256.0F / 360.0F)
        );

        sendPackets(player, spawnPacket, headRotationPacket);
    }

    public void destroy(Iterable<Player> players) {
        broadcastPackets(players, destroyPacket);
    }

    public void destroy(Player player) {
        sendPackets(player, destroyPacket);
    }

    public void dispatchPlayerInfo(Iterable<Player> players, PlayerInfoAction action) {
        broadcastPackets(
          players,
          getPlayerInfoPackets(action)
        );
    }

    public void dispatchPlayerInfo(Player player, PlayerInfoAction action) {
        sendPackets(
          player,
          getPlayerInfoPackets(action)
        );
    }

    public Packet[] getPlayerInfoPackets(PlayerInfoAction action) {
        PacketPlayOutPlayerInfo infoPacket = new PacketPlayOutPlayerInfo(
          PacketPlayOutPlayerInfo.EnumPlayerInfoAction.values()[action.ordinal()],
          parent
        );

        Packet packet = null;
        if(action == PlayerInfoAction.ADD_PLAYER) {
            packet = parent.getTeamPacket();
        } else if(action == PlayerInfoAction.REMOVE_PLAYER) {
            packet = parent.getDestroyTeamPacket();
        }

        return new Packet[] {infoPacket, packet};
    }

    public void updateMetadata(Iterable<Player> players) {
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(
          parent.getId(), parent.getDataWatcher(), true
        );

        broadcastPackets(players, metadataPacket);
    }

    public void updateMetadata(Player player) {
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(
          parent.getId(), parent.getDataWatcher(), true
        );

        sendPackets(player, metadataPacket);
    }

    public void updateHeadRotation(Iterable<Player> players) {
        updateHeadRotation(players, parent.aI, parent.pitch);
    }

    public void updateHeadRotation(Player player) {
        updateHeadRotation(player, parent.aI, parent.pitch);
    }

    public void updateHeadRotation(Iterable<Player> players, float yaw, float pitch) {
        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(
          parent.getId(),
          (byte) MathHelper.d(yaw * 256.0F / 360.0F),
          (byte) ((int) (pitch * 256.0F / 360.0F)),
          false
        );
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
          parent, (byte) MathHelper.d(yaw * 256.0F / 360.0F)
        );

        broadcastPackets(players, lookPacket, headRotationPacket);
    }

    public void updateHeadRotation(Player player, float yaw, float pitch) {
        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(
          parent.getId(),
          (byte) MathHelper.d(yaw * 256.0F / 360.0F),
          (byte) ((int) (pitch * 256.0F / 360.0F)),
          false
        );
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
          parent, (byte) MathHelper.d(yaw * 256.0F / 360.0F)
        );

        sendPackets(player, lookPacket, headRotationPacket);
    }

    public void broadcastPackets(Iterable<Player> players, Packet<?>... packets) {
        for(Player player : players) {
            CraftPlayer craftPlayer = ((CraftPlayer) player);

            for(Packet<?> packet : packets) {
                if(packet == null) continue;
                craftPlayer.getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void sendPackets(Player player, Packet<?>... packets) {
        CraftPlayer craftPlayer = ((CraftPlayer) player);

        for(Packet<?> packet : packets) {
            if(packet == null) continue;
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        }
    }

}
