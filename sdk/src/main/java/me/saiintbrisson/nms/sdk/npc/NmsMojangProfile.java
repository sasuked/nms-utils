package me.saiintbrisson.nms.sdk.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.NonNull;
import me.saiintbrisson.nms.api.entities.MojangProfile;
import me.saiintbrisson.nms.api.entities.MojangProperty;

import java.util.Map;
import java.util.UUID;

public class NmsMojangProfile extends MojangProfile {

    public NmsMojangProfile(UUID id, @NonNull String name) {
        super(id, name);
    }

    public GameProfile toGameProfile() {
        GameProfile gameProfile = new GameProfile(getId(), getName());

        for(Map.Entry<String, MojangProperty> entry : getPropertyMap().entrySet()) {
            MojangProperty value = entry.getValue();

            gameProfile.getProperties().put(
              entry.getKey(),
              value.isSigned()
                ? new Property(value.getName(), value.getValue(), value.getSignature())
                : new Property(value.getName(), value.getValue())
            );
        }

        return gameProfile;
    }

}
