package io.github.bnnuycorps.oasisbar.mixin;

import com.mojang.authlib.GameProfile;
import io.github.bnnuycorps.oasisbar.Thirst.ThirstManager;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V", shift = At.Shift.AFTER))
    public void tickMovementMixin(CallbackInfo info) {
        ThirstManager thirstManager = ((ThirstManagerInt) this).getThirstManager();
        if (thirstManager.hasThirst() && !this.isCreative() && thirstManager.getThirstLevel() < 6) {
            this.setSprinting(false);
        }
    }

}
