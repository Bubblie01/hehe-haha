package io.github.bnnuycorps.oasisbar.thirst.registry;


import io.github.bnnuycorps.oasisbar.thirst.identifier.NetworkPacketsIdentifiers;
import io.github.bnnuycorps.oasisbar.thirst.network.packet.DrinkWaterC2SPacket;
import io.github.bnnuycorps.oasisbar.thirst.network.packet.SwingHandS2CPacket;
import io.github.bnnuycorps.oasisbar.thirst.network.packet.SyncHUDS2CPacket;
import io.github.bnnuycorps.oasisbar.thirst.network.packet.SyncThirstS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


public class NetworkRegistry {
    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.DRINK_WATER_ID, DrinkWaterC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.SWING_HAND_ID, SwingHandS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.SYNC_THIRST_ID, SyncThirstS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.SYNC_HUD_ID, SyncHUDS2CPacket::receive);
    }
}
