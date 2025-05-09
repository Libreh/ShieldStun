package me.libreh.shieldstun.config;

import me.libreh.shieldstun.ShieldStun;
import me.libreh.shieldstun.util.Constants;

public class ConfigCache {
    private boolean stunEnabled;
    private boolean paperShieldKnockback;

    public ConfigCache() {
        reload();
    }

    public void reload() {
        try {
            this.stunEnabled = getConfigBoolean(Constants.enableStuns);
            this.paperShieldKnockback = getConfigBoolean(Constants.paperShieldKnockback);
        } catch (Exception e) {
            ShieldStun.LOGGER.error("Failed to refresh config cache, using defaults", e);
            this.stunEnabled = true;
            this.paperShieldKnockback = true;
        }
    }

    public boolean isStunEnabled() { return stunEnabled; }
    public boolean isPaperShieldKnockbackEnabled() { return paperShieldKnockback; }
    public double getKnockbackModifier() {
        return 0.5;
    }

    private boolean getConfigBoolean(String key) {
        try {
            return ConfigManager.getConfig().getBoolean(key);
        } catch (Exception e) {
            ShieldStun.LOGGER.info("Invalid boolean config key '{}', using true", key);
            return true;
        }
    }
}
