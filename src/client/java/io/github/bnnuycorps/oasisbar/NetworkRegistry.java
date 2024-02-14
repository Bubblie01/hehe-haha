package io.github.bnnuycorps.oasisbar;

import io.github.bnnuycorps.oasisbar.thirst.identifier.NetworkPacketsIdentifiers;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


public class NetworkRegistry {


    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.SWING_HAND_ID, SwingHandS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.SYNC_THIRST_ID, SyncThirstS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(NetworkPacketsIdentifiers.SYNC_HUD_ID, SyncHUDS2CPacket::receive);
    }
}
