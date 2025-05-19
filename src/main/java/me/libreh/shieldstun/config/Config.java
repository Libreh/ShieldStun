package me.libreh.shieldstun.config;

import me.libreh.shieldstun.util.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class Config {
    private final Map<String, ConfigOption<?>> options = new LinkedHashMap<>();

    public Config() {
        registerOption(new ConfigOption<>(Constants.ENABLE_STUNS, true, Boolean.class));
    }

    private void registerOption(ConfigOption<?> option) {
        options.put(option.getKey(), option);
    }

    public ConfigOption<?> getOption(String key) {
        return options.get(key);
    }

    public Map<String, ConfigOption<?>> getOptions() {
        return options;
    }

    public boolean getBoolean(String key) {
        return getTypedValue(key, Boolean.class);
    }

    private <T> T getTypedValue(String key, Class<T> type) {
        ConfigOption<?> option = options.get(key);
        if (option != null && type.isAssignableFrom(option.getType())) {
            return type.cast(option.get());
        }
        throw new IllegalArgumentException("Invalid config key or type: " + key);
    }
}