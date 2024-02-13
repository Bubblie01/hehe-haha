package io.github.bnnuycorps.oasisbar;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;


public class ClientEventRegistry {
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(new ClientTicksEventHandler());
    }
}