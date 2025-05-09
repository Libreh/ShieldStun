package me.libreh.shieldstun.config;

import me.libreh.shieldstun.ShieldStun;

public class ConfigCache {
    private boolean stunEnabled;
    private boolean paperShieldKnockback;

    public ConfigCache() {
        refresh();
        ConfigManager.addReloadListener(this::refresh);
    }

    void refresh() {
        try {
            this.stunEnabled = getConfigBoolean("enable_stuns");
            this.paperKnockback = getConfigBoolean("paper_style_knockback");
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
            return true;
        }
    }
}
