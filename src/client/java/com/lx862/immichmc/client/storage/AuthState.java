package com.lx862.immichmc.client.storage;

import com.lx862.immichmc.client.LoaderImpl;

import java.nio.file.Path;

public record AuthState(String url, String apiKey) {
    public static final Path PATH = LoaderImpl.getConfigDirectory().resolve("immichmc").resolve("auth.json");

    public boolean isValid() {
        return url != null && apiKey != null;
    }
}
