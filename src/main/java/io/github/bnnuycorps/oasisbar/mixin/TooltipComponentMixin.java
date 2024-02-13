package io.github.bnnuycorps.oasisbar.mixin;

import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstTooltipComponent;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstTooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;

@Mixin(TooltipComponent.class)
public interface TooltipComponentMixin {

    @Inject(method = "Lnet/minecraft/client/gui/tooltip/TooltipComponent;of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;", at = @At("HEAD"), cancellable = true)
    private static void ofMixin(TooltipData data, CallbackInfoReturnable<TooltipComponent> info) {
        if (data instanceof ThirstTooltipData)
            info.setReturnValue(new ThirstTooltipComponent((ThirstTooltipData) data));

    }
}
