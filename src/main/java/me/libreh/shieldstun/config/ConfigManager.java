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
    public static ConfigManager INSTANCE;

    private ConfigManager() {}
    
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("shieldstun.json");
    private final Config DEFAULT = new Config();
    private Config CONFIG;

    public boolean loadConfig() {
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
        } catch (Exception exception) {
            success = false;
            CONFIG = oldConfig;
            ShieldStun.LOGGER.error("Error while reading config!", exception);
        }
        return success;
    }

    public void saveConfig() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(CONFIG));
        } catch (Exception exception) {
            ShieldStun.LOGGER.error("Error occurred while saving config!", exception);
        }
    }

    public static ConfigManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigManager();
        }
        return INSTANCE;
    }

    public Config getConfig() {
        if (CONFIG == null) {
            return DEFAULT;
        }
        return CONFIG;
    }
}