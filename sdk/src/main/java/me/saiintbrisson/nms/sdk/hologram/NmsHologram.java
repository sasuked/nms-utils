package me.saiintbrisson.nms.sdk.hologram;

import lombok.Getter;
import me.saiintbrisson.nms.api.hologram.Hologram;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
		for (EntityArmorStand stand : stands) {
			PacketPlayOutRelEntityMove packet = new PacketPlayOutRelEntityMove(
				stand.getId(),
				relX,
				relY,
				relZ,
				false
			);
			
			for (Player player : tracking) {
			
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
	
	
	private void sendPacket(Packet packet) {
		for (Player player : tracking) {
			((CraftPlayer) player).getHandle()
				.playerConnection
				.sendPacket(packet);
		}
	}
}
