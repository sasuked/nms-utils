package me.saiintbrisson.nms.sdk.entities.npc.data;

import lombok.Builder;
import lombok.Data;
import me.saiintbrisson.nms.api.entities.MojangProfile;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import org.bukkit.Location;

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
	
	
	public static PersistentNpc of(Npc npc) {
		return PersistentNpc.builder()
			.id(npc.getNpcId())
			.name(npc.getName())
			.location(npc.getLocation())
			.profile(npc.getMojangProfile())
			.artificialIntelligence(npc.isArtificialIntelligence())
			.build();
	}
}
