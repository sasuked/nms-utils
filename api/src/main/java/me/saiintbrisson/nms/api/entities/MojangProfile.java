package me.saiintbrisson.nms.api.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MojangProfile {

    private final UUID id;
    @NonNull
    private String name;

    private Map<String, MojangProperty> propertyMap = new HashMap<>();

}
