package me.libreh.shieldstun.config;

import me.libreh.shieldstun.ModInit;
import me.libreh.shieldstun.util.Constants;

public class ConfigCache {
    private boolean stunEnabled;

    public ConfigCache() {
        reload();
    }

    public void reload() {
        try {
            this.stunEnabled = getConfigBoolean(Constants.ENABLE_STUNS);
        } catch (Exception e) {
            ModInit.LOGGER.error("Failed to refresh config cache, using defaults", e);
            this.stunEnabled = true;
        }
    }

    public boolean isStunEnabled() { return stunEnabled; }

    private boolean getConfigBoolean(String key) {
        try {
            return ConfigManager.getConfig().getBoolean(key);
        } catch (Exception e) {
            ModInit.LOGGER.info("Invalid boolean config key '{}', using true", key);
            return true;
        }
    }
}
