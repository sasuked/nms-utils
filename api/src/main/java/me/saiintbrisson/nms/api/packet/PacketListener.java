package me.saiintbrisson.nms.api.packet;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public interface PacketListener {

    static Object getField(Object object, String fieldName) {
        try {

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            Object value = field.get(object);

            field.setAccessible(false);

            return value;

        } catch (Exception ignore) {}

        return null;
    }

    /**
     * Method called when the server receives a packet from a player.
     *
     * @param player the player who sent the packet.
     * @param packet the packet received.
     * @return true if the event should be cancelled.
     */
    boolean read(Player player, Object packet);

    /**
     * Method called when the server sends a packet to a player.
     *
     * @param player the player who will receive the packet.
     * @param object the packet sent.
     * @return true if the event should be cancelled;
     */
    boolean write(Player player, Object object);

}
