package com.easeon.cs.silentsign;

import com.easeon.cs.core.api.definitions.enums.EventPhase;
import com.easeon.cs.core.api.events.EaseonBlockUseClient;
import com.easeon.cs.core.api.events.EaseonScreenInitClient;
import net.fabricmc.api.ClientModInitializer;

public class Easeon implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EaseonBlockUseClient.register(EventPhase.BEFORE, SilentSignHandler::register);
        EaseonScreenInitClient.register(EventPhase.AFTER, SilentSignHandler::screen);
    }
}