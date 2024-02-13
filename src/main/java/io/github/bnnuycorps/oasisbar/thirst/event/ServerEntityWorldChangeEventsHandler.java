package io.github.bnnuycorps.oasisbar.thirst.event;

import io.github.bnnuycorps.oasisbar.thirst.access.ServerPlayerAccess;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;

public class ServerEntityWorldChangeEventsHandler implements ServerEntityWorldChangeEvents.AfterPlayerChange {

    // Sync the thirst when the player change dimensions
    @Override
    public void afterChangeWorld(ServerPlayerEntity serverPlayerEntity, ServerWorld origin, ServerWorld destination) {
        ((ServerPlayerAccess) serverPlayerEntity).syncThirstAfterChangeDimension();
    }
}
