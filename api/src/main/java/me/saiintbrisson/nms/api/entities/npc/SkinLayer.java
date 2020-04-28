package me.saiintbrisson.nms.api.entities.npc;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SkinLayer {

    CAPE(0x01),

    JACKET(0x02),

    LEFT_SLEEVE(0x04),
    RIGHT_SLEEVE(0x08),

    LEFT_PANTS(0x10),
    RIGHT_PANTS(0x20),

    HAT(0x040),

    ALL(0xFF);

    private int flag;

    public byte getFlag() {
        return (byte) flag;
    }

}
