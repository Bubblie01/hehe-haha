package io.github.bnnuycorps.oasisbar.thirst.registry;


import io.github.bnnuycorps.oasisbar.thirst.identifier.NetworkPacketsIdentifiers;
import io.github.bnnuycorps.oasisbar.thirst.network.packet.DrinkWaterC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


public class ServerNetworkRegistry {
    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.DRINK_WATER_ID, DrinkWaterC2SPacket::receive);
    }

}
