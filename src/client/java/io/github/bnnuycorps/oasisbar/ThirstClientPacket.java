package io.github.bnnuycorps.oasisbar;

import java.util.ArrayList;
import java.util.List;

import io.github.bnnuycorps.oasisbar.Main;
import io.github.bnnuycorps.oasisbar.Thirst.ThirstManager;
import io.github.bnnuycorps.oasisbar.Thirst.event.HydrationTemplate;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import io.github.bnnuycorps.oasisbar.Thirst.packets.ThirstServerPacket;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

@Environment(EnvType.CLIENT)
public class ThirstClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ThirstServerPacket.THIRST_UPDATE, (client, handler, buffer, responseSender) -> {
            int[] bufferArray = buffer.readIntArray();
            int entityId = bufferArray[0];
            int thirstLevel = bufferArray[1];
            client.execute(() -> {
                if (client.player.getWorld().getEntityById(entityId) != null) {
                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
                    ThirstManager thirstManager = ((ThirstManagerInt) player).getThirstManager();
                    thirstManager.setThirstLevel(thirstLevel);
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ThirstServerPacket.EXCLUDED_SYNC, (client, handler, buffer, responseSender) -> {
            boolean setThirst = buffer.readBoolean();
            client.execute(() -> {
                ((ThirstManagerInt) client.player).getThirstManager().setThirst(setThirst);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ThirstServerPacket.HYDRATION_TEMPLATE_SYNC, (client, handler, buffer, responseSender) -> {
            List<HydrationTemplate> hydrationTemplates = new ArrayList<HydrationTemplate>();
            IntList intList = buffer.readIntList();
            for (int i = 0; i < intList.size(); i += 2) {
                List<Item> items = new ArrayList<Item>();
                for (int u = 0; u < intList.getInt(i + 1); u++) {
                    items.add(Registries.ITEM.get(buffer.readIdentifier()));
                }
                hydrationTemplates.add(new HydrationTemplate(intList.getInt(i), items));
            }
            client.execute(() -> {
                Main.HYDRATION_TEMPLATES.clear();
                Main.HYDRATION_TEMPLATES.addAll(hydrationTemplates);
            });
        });
    }
}
