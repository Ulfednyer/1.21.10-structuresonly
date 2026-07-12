package com.chaosforge.structuresonly.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    private static ConfigInstance instance;

    public static class ConfigInstance {
        public boolean keepStructureBlocks = true;
        public boolean luckyChestDrops = true;
        public boolean spawnStarterChest = true;
        public boolean loyaltyTridentVoidReturn = true;
    }

    public static void load() {
        configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "structuresonly.json");
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                instance = GSON.fromJson(reader, ConfigInstance.class);
                if (instance == null) {
                    instance = new ConfigInstance();
                }
                // Save config to write any missing default fields
                save();
            } catch (Exception e) {
                instance = new ConfigInstance();
            }
        } else {
            instance = new ConfigInstance();
            save();
        }
    }

    public static void save() {
        try {
            File parent = configFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(instance, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigInstance get() {
        if (instance == null) {
            load();
        }
        return instance;
    }
}
