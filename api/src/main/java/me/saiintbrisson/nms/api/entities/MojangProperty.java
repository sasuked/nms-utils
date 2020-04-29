package me.saiintbrisson.nms.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class MojangProperty {

    private final String name, value;
    private String signature;

    public boolean isSigned() {
        return signature != null;
    }

}
