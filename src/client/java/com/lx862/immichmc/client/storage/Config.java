package com.lx862.immichmc.client.storage;

import com.lx862.immichmc.client.LoaderImpl;

import java.nio.file.Path;

public class Config {
    public static final Path PATH = LoaderImpl.getConfigDirectory().resolve("immichmc").resolve("config.json");
    public String assignedAlbum = null;
    public boolean showUploadMessage = false;
}
