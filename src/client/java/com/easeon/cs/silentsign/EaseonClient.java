// Easeon.java
package com.easeon.cs.silentsign;

import net.fabricmc.api.ClientModInitializer;

public class EaseonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemSignEditBlockerHandler.register();
    }
}