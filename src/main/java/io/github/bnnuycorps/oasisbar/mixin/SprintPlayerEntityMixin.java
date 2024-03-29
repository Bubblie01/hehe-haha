package io.github.bnnuycorps.oasisbar.mixin;

import io.github.bnnuycorps.oasisbar.SprintManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SprintPlayerEntityMixin extends Entity implements SprintManager {

    @Unique
    private float oasisbar$sprintLevel = 18.0f;
    private int tickCounter = 0;

    public SprintPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = "Lnet/minecraft/entity/player/PlayerEntity;tickMovement()V", at = @At("TAIL"))

    private void sprintModifier(CallbackInfo ci) {
        tickCounter++;
        if(this.isSprinting() && (oasisbar$sprintLevel > 0)) {
            oasisbar$sprintLevel -= 0.1;

        }
        else if(oasisbar$sprintLevel < 18.0f) {
                if(oasisbar$sprintLevel < 0) {
                        if (tickCounter % 40 == 0) {
                            tickCounter = 0;
                            oasisbar$sprintLevel += 0.1;
                        }
                }
                else {
                    oasisbar$sprintLevel += 0.1;
                }
        }
    }

    @Override
    public float oasisbar$getSprintLevel() {
        return oasisbar$sprintLevel;
    }
}
