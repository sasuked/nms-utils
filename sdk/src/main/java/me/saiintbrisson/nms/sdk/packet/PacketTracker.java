package me.saiintbrisson.nms.sdk.packet;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketTracker {

    public void inject(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        new PacketInterceptor(craftPlayer.getHandle());
    }

    private static Object getField(Object object, String fieldName) {
        try {

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            Object value = field.get(object);

            field.setAccessible(false);

            return value;

        } catch (Exception ignore) {}

        return null;
    }

}
