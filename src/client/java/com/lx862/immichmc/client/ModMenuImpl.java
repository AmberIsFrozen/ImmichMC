package com.lx862.immichmc.client;

import com.lx862.immichmc.client.gui.screen.ConfigScreen;
import com.lx862.immichmc.client.gui.screen.LandingScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            boolean shouldLogin = ImmichMC.getClient() == null;
            return (shouldLogin ? new LandingScreen() : new ConfigScreen()).withPreviousScreen(screen);
        };
    }
}
