package io.github.bnnuycorps.oasisbar.thirst.registry;

import io.github.bnnuycorps.oasisbar.thirst.event.ServerEntityWorldChangeEventsHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;


public class ServerEventRegistry {
    public static void register() {
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(new ServerEntityWorldChangeEventsHandler());
    }
}
