package io.github.bnnuycorps.oasisbar.thirst.network.utils;

import io.github.bnnuycorps.oasisbar.thirst.access.ThirstManagerAccess;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class defaultBuffers {
    public static PacketByteBuf createSyncThirstDefaultBuffer(ServerPlayerEntity serverPlayerEntity) {
        // Create a new buffer with a int array, where will take the Player ID (int) and the current player thristLevel (int)
        PacketByteBuf buffer = PacketByteBufs.create();

        buffer.writeIntArray(new int[]{
                serverPlayerEntity.getId(), ((ThirstManagerAccess) serverPlayerEntity).getThirstManager().getThirstLevel()
        });

        return buffer;
    }
}
