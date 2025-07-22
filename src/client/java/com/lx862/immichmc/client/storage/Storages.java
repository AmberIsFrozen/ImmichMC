package com.lx862.immichmc.client.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.lx862.immichmc.client.ImmichMC;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Storages {
    public static AuthState authState = new AuthState(null, null);
    public static Config config = new Config();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static void load() {
        try {
            Files.createDirectories(AuthState.PATH.getParent());
            Files.createDirectories(Config.PATH.getParent());
            try(FileReader fileReader = new FileReader(AuthState.PATH.toFile()); JsonReader jsonReader = new JsonReader(fileReader)) {
                authState = gson.fromJson(jsonReader, AuthState.class);
            }
            try(FileReader fileReader = new FileReader(Config.PATH.toFile()); JsonReader jsonReader = new JsonReader(fileReader)) {
                config = gson.fromJson(jsonReader, Config.class);
            }
        } catch (IOException e) {
            save();
        }
    }

    public static void save() {
        try(FileWriter fileWriter = new FileWriter(AuthState.PATH.toFile())) {
            gson.toJson(authState, fileWriter);
        } catch (IOException ex) {
            ImmichMC.LOGGER.error("Failed to save auth state!", ex);
        }
        try(FileWriter fileWriter = new FileWriter(Config.PATH.toFile())) {
            gson.toJson(config, fileWriter);
        } catch (IOException ex) {
            ImmichMC.LOGGER.error("Failed to save config!", ex);
        }
    }
}
