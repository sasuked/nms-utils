package me.saiintbrisson.nms.api.entities.npc;

import me.saiintbrisson.nms.api.entities.MojangProfile;
import me.saiintbrisson.nms.api.entities.NmsEntity;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
public
interface Npc extends NmsEntity {

    Set<UUID> getVisibleTo();

    MojangProfile getMojangProfile();

    String getName();
    UUID getUniqueId();

    Location getLocation();
    void setLocation(Location location);

    void setSkinLayers(SkinLayer... layers);
    default void setSkin(String value, String signature) {
        setProperty("textures", value, signature);
    }

    void setProperty(String name, String value, String signature);

    boolean isArtificialIntelligence();
    void setArtificialIntelligence(boolean artificialIntelligence);

    int getTargetRadius();
    void setTargetRadius(int radius);

    boolean isInvincible();
    void setInvincible(boolean invincible);

    void trackEntity();

    void lookAtEntity(LivingEntity entity);

    void dispatchPlayerInfo(Iterable<Player> players, PlayerInfoAction action);

    void dispatchPlayerInfo(Player player, PlayerInfoAction action);

    void spawn(Iterable<Player> players);

    void spawn(Player player);

    void destroy(Iterable<Player> players);

    void destroy(Player player);

    void updateMetadata(Iterable<Player> players);

    void updateMetadata(Player player);

    void updateHeadRotation(Iterable<Player> players);

    void updateHeadRotation(Player player);

}
