package me.libreh.shieldstun.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.libreh.shieldstun.ShieldStun;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("%s.json".formatted(ShieldStun.MOD_ID));
    private static Config CONFIG;

    public static Config getConfig() {
        if (CONFIG == null) {
            return Config.DEFAULT;
        }
        return CONFIG;
    }

    public static boolean loadConfig() {
        Config oldConfig = CONFIG;
        boolean success;

        CONFIG = null;
        try {
            File configFile = CONFIG_PATH.toFile();

            CONFIG = configFile.exists() ? GSON.fromJson(new InputStreamReader(new FileInputStream(configFile), "UTF-8"), Config.class) : new Config();
            saveConfig();

            success = true;
        } catch (Exception exception) {
            success = false;
            CONFIG = oldConfig;
            ShieldStun.LOGGER.error("Something went wrong while reading config!", exception);
        }

        return success;
    }

    public static boolean saveConfig() {
        boolean success;

        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(CONFIG));
            success = true;
        } catch (Exception exception) {
            success = false;
            ShieldStun.LOGGER.error("Something went wrong while saving config!", exception);
        }

        return success;
    }
}