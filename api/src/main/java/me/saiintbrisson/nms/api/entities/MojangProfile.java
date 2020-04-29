package me.saiintbrisson.nms.api.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public class MojangProfile {

    private transient final UUID id;
    @NonNull
    private transient String name;

    private Map<String, MojangProperty> propertyMap = new HashMap<>();

}
