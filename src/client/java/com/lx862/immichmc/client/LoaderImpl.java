package com.lx862.immichmc.client;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class LoaderImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
