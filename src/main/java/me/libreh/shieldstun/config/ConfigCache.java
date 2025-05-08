package me.libreh.shieldstun.config;

import me.libreh.shieldstun.ShieldStun;

public class ConfigCache {
    private boolean stunEnabled;
    private boolean paperKnockback;
    private double knockbackModifier = 0.5;

    public ConfigCache() {
        refresh();
        ConfigManager.addReloadListener(this::refresh);
    }

    void refresh() {
        try {
            this.stunEnabled = getConfigBoolean("enable_stuns");
            this.paperKnockback = getConfigBoolean("paper_style_knockback");
        } catch (Exception e) {
            ShieldStun.LOGGER.error("Failed to refresh config cache, using defaults", e);
            this.stunEnabled = true;
            this.paperKnockback = true;
        }
    }

    public boolean isStunEnabled() { return stunEnabled; }
    public boolean isPaperKnockbackEnabled() { return paperKnockback; }
    public double getKnockbackModifier() { return knockbackModifier; }

    private boolean getConfigBoolean(String key) {
        try {
            return ConfigManager.getConfig().getBoolean(key);
        } catch (Exception e) {
            return true;
        }
    }
}
