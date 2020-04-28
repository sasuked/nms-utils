package me.saiintbrisson.nms.sdk.entities.npc.controllers;

import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.NetworkManager;

public class NetworkController extends NetworkManager {

    public NetworkController() {
        super(EnumProtocolDirection.CLIENTBOUND);


    }

}
