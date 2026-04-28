package me.libreh.shieldstun.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import me.libreh.shieldstun.ShieldStun;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    public static final String CONFIG_NAME = "shieldstun.json";
    public static final Path CONFIG_PATH = CONFIG_DIR.resolve(CONFIG_NAME);
    private static Config CONFIG;

    public static boolean load() {
        Config oldConfig = CONFIG;
        boolean success;

        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            Config config = GSON.fromJson(reader, Config.class);
            if (config == null) {
                ShieldStun.LOGGER.error("Failed to load " + CONFIG_NAME + ": Config parsed as null");
                CONFIG = oldConfig;
                return false;
            }
            CONFIG = config;
            save();
            success = true;
        } catch (FileNotFoundException e) {
            ShieldStun.LOGGER.info("Creating default config " + CONFIG_NAME);
            CONFIG = new Config();
            save();
            success = true;
        } catch (IOException | JsonSyntaxException e) {
            ShieldStun.LOGGER.error("Failed to read config " + CONFIG_NAME, e);
            CONFIG = oldConfig;
            success = false;
        }

        return success;
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(CONFIG);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            ShieldStun.LOGGER.error("Failed to save config " + CONFIG_NAME, e);
        }
    }

    public static Config getConfig() {
        return CONFIG;
    }
}
