package me.saiintbrisson.nms.sdk.scoreboard;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.AsyncCatcher;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class NmsScoreboard extends Scoreboard implements me.saiintbrisson.nms.api.scoreboard.Scoreboard {
	
	private ScoreboardLine[] lines;
	
	private Map<UUID, org.bukkit.scoreboard.Scoreboard> watching;
	private ScheduledFuture scheduledFuture;
	
	private ScoreboardObjective objective;
	
	public NmsScoreboard(String name, String title) {
		this.lines = new ScoreboardLine[21];
		
		this.watching = new ConcurrentHashMap<>();
		this.objective = new NmsScoreboardObjective(this, name, title);
	}
	
	@Override
	public void setTitle(String title) {
		objective.setDisplayName(title);
	}
	
	@Override
	public void setUpdateDelay(long updateDelay) {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
		}
		
		scheduledFuture = Executors
			.newSingleThreadScheduledExecutor()
			.scheduleWithFixedDelay(
				this::update,
				1000,
				updateDelay,
				TimeUnit.MILLISECONDS)
		;
	}
	
	@Override
	public void setLine(int slot, String line) {
		lines[20 - slot] = new ScoreboardLine(line);
	}
	
	@Override
	public void setLine(int slot, Function<Player, String> line) {
		lines[20 - slot] = new ScoreboardLine(line);
	}
	
	@Override
	public void setLine(int slot, String line, Function<Player, String> functionLine) {
		lines[20 - slot] = new ScoreboardLine(line, functionLine);
	}
	
	@Override
	public void blankLine(int slot) {
		lines[20 - slot] = new ScoreboardLine("");
	}
	
	@Override
	public void show(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		AsyncCatcher.enabled = false;
		org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		craftPlayer.setScoreboard(scoreboard);
		
		PacketPlayOutScoreboardObjective objectivePacket = new PacketPlayOutScoreboardObjective(objective, 0);
		craftPlayer.getHandle().playerConnection.sendPacket(objectivePacket);
		
		for (int i = 0; i < lines.length; i++) {
			ScoreboardLine line = lines[i];
			if (line == null) continue;
			
			String text = line.getText(player);
			if (text == null) continue;
			
			NmsTeam team = new NmsTeam(this, i, line, player);
			PacketPlayOutScoreboardTeam teamPacket = new PacketPlayOutScoreboardTeam(team, 0);
			
			ScoreboardScore score = new ScoreboardScore(this, objective, team.getMiddle());
			score.setScore(i);
			PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(score);
			
			craftPlayer.getHandle().playerConnection.sendPacket(teamPacket);
			craftPlayer.getHandle().playerConnection.sendPacket(scorePacket);
		}
		
		PacketPlayOutScoreboardDisplayObjective displayPacket =
			new PacketPlayOutScoreboardDisplayObjective(1, objective);
		
		craftPlayer.getHandle().playerConnection.sendPacket(displayPacket);
		
		watching.put(player.getUniqueId(), scoreboard);
	}
	
	@Override
	public void update() {
		for (Map.Entry<UUID, org.bukkit.scoreboard.Scoreboard> entry : watching.entrySet()) {
			Player player = Bukkit.getPlayer(entry.getKey());
			if (player == null) {
				watching.remove(entry.getKey());
				continue;
			}
			
			update(player);
		}
	}
	
	@Override
	public void update(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		org.bukkit.scoreboard.Scoreboard scoreboard = watching.get(craftPlayer.getUniqueId());
		if (scoreboard == null) return;
		
		if (craftPlayer.getScoreboard() != scoreboard) {
			PacketPlayOutScoreboardObjective objectivePacket = new PacketPlayOutScoreboardObjective(objective, 1);
			craftPlayer.getHandle().playerConnection.sendPacket(objectivePacket);
			
			watching.remove(craftPlayer.getUniqueId());
			return;
		}
		
		for (int i = 0; i < lines.length; i++) {
			ScoreboardLine line = lines[i];
			if (line == null || !line.isUpdatable()) continue;
			
			String text = line.getText(player);
			if (text == null) continue;
			
			NmsTeam team = new NmsTeam(this, i, line, player);
			PacketPlayOutScoreboardTeam teamPacket = new PacketPlayOutScoreboardTeam(team, 2);
			
			craftPlayer.getHandle().playerConnection.sendPacket(teamPacket);
		}
	}
	
	@Override
	public void update(Player player, int slot) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		org.bukkit.scoreboard.Scoreboard scoreboard = watching.get(craftPlayer.getUniqueId());
		if (scoreboard == null) return;
		
		if (craftPlayer.getScoreboard() != scoreboard) {
			PacketPlayOutScoreboardObjective objectivePacket = new PacketPlayOutScoreboardObjective(objective, 1);
			craftPlayer.getHandle().playerConnection.sendPacket(objectivePacket);
			
			watching.remove(craftPlayer.getUniqueId());
			return;
		}
		
		ScoreboardLine line = lines[slot];
		if (line == null || !line.isUpdatable()) return;
		
		String text = line.getText(player);
		if (text == null) return;
		
		NmsTeam team = new NmsTeam(this, slot, line, player);
		PacketPlayOutScoreboardTeam teamPacket = new PacketPlayOutScoreboardTeam(team, 2);
		
		craftPlayer.getHandle().playerConnection.sendPacket(teamPacket);
	}
	
	@Override
	public void remove(Player player) {
		AsyncCatcher.enabled = false;
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		
		CraftPlayer craftPlayer = (CraftPlayer) player;
		
		for (int i = 0; i < lines.length; i++) {
			ScoreboardLine line = lines[i];
			if (line == null) continue;
			
			NmsTeam team = new NmsTeam(this, i, line, player);
			PacketPlayOutScoreboardTeam teamPacket = new PacketPlayOutScoreboardTeam(team, 1);
			PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(team.getMiddle());
			
			craftPlayer.getHandle().playerConnection.sendPacket(teamPacket);
			craftPlayer.getHandle().playerConnection.sendPacket(scorePacket);
		}
		
		PacketPlayOutScoreboardObjective objectivePacket = new PacketPlayOutScoreboardObjective(objective, 1);
		craftPlayer.getHandle().playerConnection.sendPacket(objectivePacket);
		
		watching.remove(player.getUniqueId());
	}
	
	@Override
	public void handleObjectiveChanged(ScoreboardObjective scoreboardObjective) {
		for (Map.Entry<UUID, org.bukkit.scoreboard.Scoreboard> entry : watching.entrySet()) {
			Player player = Bukkit.getPlayer(entry.getKey());
			if (player == null || player.getScoreboard() != entry.getValue()) {
				watching.remove(entry.getKey());
				continue;
			}
			
			CraftPlayer craftPlayer = (CraftPlayer) player;
			PacketPlayOutScoreboardObjective objectivePacket =
				new PacketPlayOutScoreboardObjective(objective, 2);
			craftPlayer.getHandle().playerConnection.sendPacket(objectivePacket);
		}
	}
	
}
