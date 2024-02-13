package io.github.bnnuycorps.oasisbar;

import io.github.bnnuycorps.oasisbar.Thirst.inits.ModelProviderInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ClientInitializer implements ClientModInitializer {
	int scaledWidth = 0;
	int scaledHeight = 0;
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		HudRenderCallback.EVENT.register((drawContext, delta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			client.getProfiler().push("sprintBar");
			scaledHeight = drawContext.getScaledWindowHeight();
			scaledWidth = drawContext.getScaledWindowWidth();
			int sprintHeight = scaledHeight - 28 + 2;
			int sprintWidth = scaledWidth/2 - 91;
			drawContext.drawTexture(new Identifier("textures/gui/icons.png"), sprintWidth, sprintHeight,0,112,182,5);
			PlayerEntity player = client.player;
			int k = (int)(((SprintManager)player).oasisbar$getSprintLevel() * 10.1f);
			if(k > 0) {
				drawContext.drawTexture(new Identifier("textures/gui/icons.png"), sprintWidth, sprintHeight+1,0,117,k,5);
			}
			client.getProfiler().pop();
		});

		ModelProviderInit.init();
		ThirstClientPacket.init();
	}
}