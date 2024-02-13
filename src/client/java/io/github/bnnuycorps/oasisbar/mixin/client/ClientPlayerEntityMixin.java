package io.github.bnnuycorps.oasisbar.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.bnnuycorps.oasisbar.SprintManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @ModifyReturnValue(method = "canSprint", at = @At("RETURN"))
    private boolean cancelSprint(boolean original) {
        float sprintLevel = ((SprintManager)this).oasisbar$getSprintLevel();
        if(sprintLevel < 0.0f)
            return false;
        return original;
    }
}
