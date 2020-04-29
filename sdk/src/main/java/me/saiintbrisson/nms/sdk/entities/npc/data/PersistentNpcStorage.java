package me.saiintbrisson.nms.sdk.entities.npc.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.saiintbrisson.nms.api.entities.NpcRegistry;
import me.saiintbrisson.nms.api.entities.npc.Npc;
import me.saiintbrisson.nms.api.entities.npc.PersistentNpc;
import me.saiintbrisson.nms.sdk.NmsPlugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.file.Files.*;

public class PersistentNpcStorage {

    private final Path path;
    private final Gson gson;
    private NpcRegistry registry;

    private static PersistentNpcStorage instance;

    public PersistentNpcStorage() {
        path = Paths.get(NmsPlugin.getInstance().getDataFolder() + "/npcs.json");
        gson = new GsonBuilder()
          .serializeSpecialFloatingPointValues()
          .enableComplexMapKeySerialization()
          .setPrettyPrinting()
          .create();
        registry = NpcRegistry.getInstance();

        try {
            File file = path.toFile();
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Set<PersistentNpc> loadAllNpcs() {
        TypeToken<Set<PersistentNpc>> token = new WrappedNpcSetToken();

        try(Reader reader = newBufferedReader(path)) {
            Set<PersistentNpc> fromJson = gson.fromJson(reader, token.getType());
            if(fromJson != null) {
                return fromJson;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }

    public void saveAllNpcs() {
        TypeToken<Set<PersistentNpc>> token = new WrappedNpcSetToken();

        Set<PersistentNpc> persistentNpcs = registry.getAll()
          .stream()
          .filter(Npc::isVisibleToAll)
          .filter(Npc::isInvincible)
          .map(PersistentNpc::of)
          .collect(Collectors.toSet());

        try(Writer writer = newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            gson.toJson(persistentNpcs, token.getType(), writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PersistentNpcStorage getInstance() {
        if(instance == null) {
            instance = new PersistentNpcStorage();
        }

        return instance;
    }

    private class WrappedNpcSetToken extends TypeToken<Set<PersistentNpc>> {
        // dummy class
    }
}
