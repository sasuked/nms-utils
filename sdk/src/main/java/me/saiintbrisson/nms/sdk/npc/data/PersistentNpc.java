package me.saiintbrisson.nms.sdk.npc.data;

import lombok.Builder;
import lombok.Data;
import me.saiintbrisson.nms.api.NmsAPI;
import me.saiintbrisson.nms.api.entities.MojangProfile;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.sdk.npc.NmsNpc;
import org.bukkit.Location;
import org.bukkit.World;

import static me.saiintbrisson.nms.api.NmsAPI.createNpc;

@Data
@Builder
public class PersistentNpc {
	
	private int id;
	private String prefix;
	private String name;
	private String complement;
	private Location location;
	private MojangProfile profile;
	private boolean artificialIntelligence;
	
	
	public static PersistentNpc of(NmsNpc npc) {
		return PersistentNpc.builder()
			.id(npc.getId())
			.name(npc.getName())
			.location(npc.getLocation())
			.profile(npc.getMojangProfile())
			.artificialIntelligence(npc.isArtificialIntelligence())
			.build();
	}
}
