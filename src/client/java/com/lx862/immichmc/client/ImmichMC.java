package com.lx862.immichmc.client;

import com.lx862.immichmc.client.immich.ImmichClient;
import com.lx862.immichmc.client.storage.Storages;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmichMC implements ClientModInitializer {
    public static final String BRAND = "ImmichMC";
    public static final Logger LOGGER = LoggerFactory.getLogger(BRAND);
    private static ImmichClient client;

    @Override
    public void onInitializeClient() {
        Storages.load();
        if(Storages.authState.isValid()) {
            client = new ImmichClient(Storages.authState.url(), Storages.authState.apiKey());
        }
    }

    public static ImmichClient getClient() {
        return client;
    }

    public static void setClient(ImmichClient newClient) {
        client = newClient;
    }
}
