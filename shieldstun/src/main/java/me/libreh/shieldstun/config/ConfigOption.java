package me.libreh.shieldstun.config;

public class ConfigOption<T> {
    private final String key;
    private final T defaultValue;
    private T value;
    private final Class<T> type;

    public ConfigOption(String key, T defaultValue, Class<T> type) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.type = type;
        this.value = defaultValue;
    }

    public String getKey() { return key; }
    public T getDefault() { return defaultValue; }
    public T get() { return value; }
    public void set(T value) { this.value = value; }
    public Class<T> getType() { return type; }
}