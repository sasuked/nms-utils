package me.saiintbrisson.nms.sdk.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.packet.PacketListener;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PacketInterceptor extends ChannelDuplexHandler {

    private static final Map<Plugin, PacketListener> LISTENER_MAP = new HashMap<>();

    public static Collection<PacketListener> getPacketListeners() {
        return LISTENER_MAP.values();
    }

    public static void registerPacketListener(Plugin owner, PacketListener packetListener) {
        if(LISTENER_MAP.containsKey(owner)) {
            throw new IllegalStateException("Plugin already registered");
        }

        LISTENER_MAP.put(owner, packetListener);
    }

    private EntityPlayer entityPlayer;

    public PacketInterceptor(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;

        entityPlayer
          .playerConnection
          .networkManager
          .channel
          .pipeline()
          .addBefore(
            "packet_handler",
            "packet_interceptor",
            this
          );
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        for(PacketListener value : LISTENER_MAP.values()) {
            if(value.read(entityPlayer.getBukkitEntity(), o)) return;
        }

        super.channelRead(channelHandlerContext, o);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object o, ChannelPromise promise) throws Exception {
        for(PacketListener value : LISTENER_MAP.values()) {
            if(value.write(entityPlayer.getBukkitEntity(), o)) return;
        }

        super.write(ctx, o, promise);
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
