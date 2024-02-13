package io.github.bnnuycorps.oasisbar.mixin;


import io.github.bnnuycorps.oasisbar.thirst.access.ItemMaxCount;
import io.github.bnnuycorps.oasisbar.thirst.access.ThirstManagerAccess;
import io.github.bnnuycorps.oasisbar.thirst.identifier.NetworkPacketsIdentifiers;
import io.github.bnnuycorps.oasisbar.thirst.network.utils.defaultBuffers;
import io.github.bnnuycorps.oasisbar.thirst.registry.ConfigRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements ThirstManagerAccess {

    // After the player connects to the world
    @Inject(method = "onPlayerConnect", at = @At(value = "TAIL"))
    private void vanillaThirst$afterThePlayerConnectsTheWorld(ClientConnection connection, ServerPlayerEntity serverPlayerEntity, CallbackInfo cir) {

        // Send a S2C packet to sync the thirst
        PacketByteBuf buffer = defaultBuffers.createSyncThirstDefaultBuffer(serverPlayerEntity);
        ServerPlayNetworking.send(serverPlayerEntity, NetworkPacketsIdentifiers.SYNC_THIRST_ID, buffer);

        // Check if the player can stack glass
        if (ConfigRegistry.CONFIG.can_stack_glass) {
            ((ItemMaxCount) Items.GLASS_BOTTLE).setMaxCount(ConfigRegistry.CONFIG.can_stack_glass_factor);
        } else {
            ((ItemMaxCount) Items.GLASS_BOTTLE).setMaxCount(1);
        }

        // Check if the player can stack potions
        if (ConfigRegistry.CONFIG.can_stack_potion) {
            ((ItemMaxCount) Items.POTION).setMaxCount(ConfigRegistry.CONFIG.can_stack_potion_factor);
        } else {
            ((ItemMaxCount) Items.POTION).setMaxCount(1);
        }

    }
}
