package me.saiintbrisson.nms.sdk.entities.npc.controllers;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class ConnectionController extends PlayerConnection {

    public ConnectionController(EntityPlayer entityplayer) {
        super(MinecraftServer.getServer(), new NetworkController(), entityplayer);
    }

    @Override
    public void sendPacket(Packet packet) {}

}
