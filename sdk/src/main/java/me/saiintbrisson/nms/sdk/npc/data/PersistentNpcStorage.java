package me.saiintbrisson.nms.sdk.npc.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.sdk.NmsPlugin;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;

public class PersistentNpcStorage {
	
	private final String mainDir;
	private final Gson gson;
	private NpcRegistry registry;
	
	private static PersistentNpcStorage instance;
	
	public PersistentNpcStorage() {
		mainDir = NmsPlugin.getInstance().getDataFolder() + "/";
		gson = new GsonBuilder()
			.serializeSpecialFloatingPointValues()
			.enableComplexMapKeySerialization()
			.setPrettyPrinting()
			.create();
		registry = NpcRegistry.getInstance();
	}
	
	
	public Set<PersistentNpc> loadAllNpcs() {
		TypeToken<Set<PersistentNpc>> token = new WrappedNpcSetToken();
		
		Set<PersistentNpc> persistentNpcs = new HashSet<>();
		try (Reader reader = newBufferedReader(Paths.get(mainDir + "npcs.json"))) {
			return gson.fromJson(reader, token.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return persistentNpcs;
	}
	
	public void saveAllNpcs() {
		TypeToken<Set<PersistentNpc>> token = new WrappedNpcSetToken();
		
		Set<PersistentNpc> persistentNpcs = registry.getAll()
			.stream()
			.map(PersistentNpc::of)
			.collect(Collectors.toSet());
		
		try (Writer writer = newBufferedWriter(Paths.get(mainDir + "npcs.json"))) {
			gson.toJson(persistentNpcs, token.getType(), writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PersistentNpcStorage getInstance() {
		if(instance == null){
			instance = new PersistentNpcStorage();
		}
		
		return instance;
	}
	
	private class WrappedNpcSetToken extends TypeToken<Set<PersistentNpc>> {
		// dummy class
	}
}
