package me.saiintbrisson.nms.sdk;

import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.NmsHolder;
import me.saiintbrisson.nms.api.area.*;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.entities.npc.PlayerInfoAction;
import me.saiintbrisson.nms.api.packet.PacketListener;
import me.saiintbrisson.nms.api.scoreboard.Scoreboard;
import me.saiintbrisson.nms.sdk.area.NmsHistory;
import me.saiintbrisson.nms.sdk.area.SimpleEditor;
import me.saiintbrisson.nms.sdk.area.object.NmsCuboid;
import me.saiintbrisson.nms.sdk.area.object.NmsWorld;
import me.saiintbrisson.nms.sdk.entities.npc.NmsNpc;
import me.saiintbrisson.nms.sdk.packet.PacketInterceptor;
import me.saiintbrisson.nms.sdk.packet.PacketRegister;
import me.saiintbrisson.nms.sdk.scoreboard.NmsScoreboard;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.AsyncCatcher;

import java.util.Collection;

public class NmsPlugin extends JavaPlugin implements NmsHolder {
	
	public static net.minecraft.server.v1_8_R3.BlockPosition toNMSPosition(BlockPosition position) {
		return new net.minecraft.server.v1_8_R3.BlockPosition(
			position.getX(),
			position.getY(),
			position.getZ()
		);
	}
	
	public static BlockPosition toAPIPosition(net.minecraft.server.v1_8_R3.BlockPosition position) {
		return new SimpleBlockPosition(
			position.getX(),
			position.getY(),
			position.getZ()
		);
	}
	
	@Override
	public void onLoad() {
		NmsAPI.setHolder(this);
		
		registerPacketListener(this, new PacketListener() {
			@Override
			public boolean read(Player player, Object packet) {
				return false;
			}
			
			@Override
			public boolean write(Player player, Object object) {
				if (object instanceof PacketPlayOutNamedEntitySpawn) {
					PacketPlayOutNamedEntitySpawn spawnPacket = (PacketPlayOutNamedEntitySpawn) object;
					int field = (int) PacketListener.getField(spawnPacket, "a");
					
					NmsNpc nmsNpc = ((NmsNpc) NpcRegistry.getInstance().getByEntityId(field));
					if (nmsNpc != null) {
						if (!nmsNpc.isVisibleToAll() && !nmsNpc.getVisibleMap().containsKey(player.getUniqueId())) return true;
						
						nmsNpc.getVisibleMap().put(player.getUniqueId(), true);
						nmsNpc.dispatchPlayerInfo(player, PlayerInfoAction.ADD_PLAYER);
					}
				}
				
				return false;
			}
		});
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PacketRegister(), this);
		
		NpcRegistry.getInstance().startTick();
	}
	
	@Override
	public Collection<PacketListener> getPacketListeners() {
		return PacketInterceptor.getPacketListeners();
	}
	
	@Override
	public void registerPacketListener(Plugin owner, PacketListener packetListener) {
		PacketInterceptor.registerPacketListener(owner, packetListener);
	}
	
	@Override
	public AreaWorld<? extends AreaBlock> getNmsWorld(World world) {
		return new NmsWorld(world);
	}
	
	@Override
	public NmsCuboid getCuboid(AreaWorld world,
														 int lowerX, int lowerY, int lowerZ,
														 int upperX, int upperY, int upperZ) {
		if (!(world instanceof NmsWorld)) return null;
		
		return new NmsCuboid(
			(NmsWorld) world,
			lowerX, lowerY, lowerZ,
			upperX, upperY, upperZ
		);
	}
	
	@Override
	public <B extends AreaBlock> AreaEditor<B, ? extends AreaWorld<B>> getAreaEditor(AreaWorld<B> world) {
		if (!(world instanceof NmsWorld)) return null;
		
		return (AreaEditor<B, ? extends AreaWorld<B>>) new SimpleEditor((NmsWorld) world);
	}
	
	@Override
	public AreaHistory<?> getAreaHistory() {
		return new NmsHistory();
	}
	
	@Override
	public void setAsyncCatcher(boolean status) {
		AsyncCatcher.enabled = status;
	}
	
	@Override
	public Scoreboard createScoreboard(String name, String title) {
		return new NmsScoreboard(name, title);
	}
	
	@Override
	public Npc createNpc(World world, String prefix, String name, String complement) {
		return new NmsNpc(world, prefix, name, complement);
	}
	
	@Override
	public Npc createNpc(int id, World world, String prefix, String name, String complement) {
		if (NpcRegistry.getInstance().getByNpcId(id) != null) {
			throw new IllegalArgumentException("This id is already in use");
		}
		
		return new NmsNpc(id, world, prefix, name, complement);
	}
	
	public static NmsPlugin getInstance() {
		return getPlugin(NmsPlugin.class);
	}
	
}
