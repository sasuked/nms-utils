package me.saiintbrisson.nms.api.entities.npc;

import me.saiintbrisson.nms.api.entities.MojangProfile;
import me.saiintbrisson.nms.api.entities.MojangProperty;
import me.saiintbrisson.nms.api.entities.NmsEntity;
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

    void setSkinLayers(SkinLayer... layers);
    default void setSkin(String value, String signature) {
        setProperty("textures", value, signature);
    }

    void setProperty(String name, String value, String signature);
    default void setProperty(MojangProperty mojangProperty) {
        setProperty(mojangProperty.getName(), mojangProperty.getValue(), mojangProperty.getSignature());
    }

    boolean isVisibleToAll();
    void setVisibleToAll(boolean visibleToAll);

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

    void respawn();

    void destroy(Iterable<Player> players);

    void destroy(Player player);

    void updateMetadata(Iterable<Player> players);

    void updateMetadata(Player player);

    void updateHeadRotation(Iterable<Player> players);

    void updateHeadRotation(Player player);

}
