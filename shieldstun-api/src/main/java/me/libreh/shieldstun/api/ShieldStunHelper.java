package me.libreh.shieldstun.api;

public class ShieldStunHelper {
    private static boolean enabled = true;

    public static void setEnabled(boolean enabled) {
        ShieldStunHelper.enabled = enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private ShieldStunHelper() {}
}
