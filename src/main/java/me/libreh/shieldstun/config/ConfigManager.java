package me.libreh.shieldstun.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.libreh.shieldstun.ShieldStun;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_NAME = "shieldstun.json";
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_NAME);
    private static Config CONFIG;

    public static boolean loadConfig() {
        Config oldConfig = CONFIG;
        boolean success;
        CONFIG = null;
        try {
            Config config;
            File configFile = CONFIG_PATH.toFile();

            if (configFile.exists()) {
                config = GSON.fromJson(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8), Config.class);
            } else {
                config = new Config();
            }

            CONFIG = config;
            saveConfig();

            success = true;
        } catch (Exception e) {
            success = false;
            CONFIG = oldConfig;
            ShieldStun.LOGGER.error("Failed to read config " + CONFIG_NAME, e);
        }
        return success;
    }

    public static void saveConfig() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(CONFIG));
        } catch (Exception exception) {
            ShieldStun.LOGGER.error("Failed to save config " + CONFIG_NAME, exception);
        }
    }

    public static Config getConfig() {
        if (CONFIG == null) {
            return new Config();
        }
        return CONFIG;
    }
}