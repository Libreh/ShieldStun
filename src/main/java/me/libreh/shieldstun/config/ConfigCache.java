package me.libreh.shieldstun.config;

import me.libreh.shieldstun.ModInit;
import me.libreh.shieldstun.util.Constants;

public class ConfigCache {
    private boolean stunEnabled;
    private boolean paperShieldKnockback;

    public ConfigCache() {
        reload();
    }

    public void reload() {
        try {
            this.stunEnabled = getConfigBoolean(Constants.ENABLE_STUNS);
            this.paperShieldKnockback = getConfigBoolean(Constants.PAPER_SHIELD_KNOCKBACK);
        } catch (Exception e) {
            ModInit.LOGGER.error("Failed to refresh config cache, using defaults", e);
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
            ModInit.LOGGER.info("Invalid boolean config key '{}', using true", key);
            return true;
        }
    }
}
