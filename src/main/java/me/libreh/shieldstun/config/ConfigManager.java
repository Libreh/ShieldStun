package me.libreh.shieldstun.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.libreh.shieldstun.ShieldStun;
import net.fabricmc.loader.api.FabricLoader;

import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("shieldstun.json");
    private static Config config;

    public static Config getConfig() {
        if (config == null) loadConfig();
        return config;
    }

    private static final List<Runnable> RELOAD_LISTENERS = new CopyOnWriteArrayList<>();

    public static boolean loadConfig() {
        try {
            config = new Config();
            if (!Files.exists(CONFIG_PATH)) {
                saveConfig();
                return true;
            }

            JsonObject json = GSON.fromJson(new InputStreamReader(Files.newInputStream(CONFIG_PATH)), JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                ConfigOption<?> option = config.getOption(entry.getKey());
                if (option != null) {
                    setOptionValue(option, entry.getValue());
                }
            }
            notifyReloadListeners();
            return true;
        } catch (Exception e) {
            ShieldStun.LOGGER.error("Failed to load config", e);
            return false;
        }
    }

    public static void addReloadListener(Runnable listener) {
        RELOAD_LISTENERS.add(listener);
    }

    private static void notifyReloadListeners() {
        for (Runnable listener : RELOAD_LISTENERS) {
            try {
                listener.run();
            } catch (Exception e) {
                ShieldStun.LOGGER.error("Error in config reload listener", e);
            }
        }
    }

    private static void setOptionValue(ConfigOption<?> option, JsonElement element) {
        if (option.getType() == Boolean.class && element.isJsonPrimitive()) {
            ((ConfigOption<Boolean>) option).set(element.getAsBoolean());
        }
    }

    public static boolean saveConfig() {
        try {
            JsonObject json = new JsonObject();
            for (ConfigOption<?> option : config.getOptions().values()) {
                if (option.getType() == Boolean.class) {
                    json.addProperty(option.getKey(), (Boolean) option.get());
                }
            }
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(json));
            return true;
        } catch (Exception e) {
            ShieldStun.LOGGER.error("Failed to save config", e);
            return false;
        }
    }
}