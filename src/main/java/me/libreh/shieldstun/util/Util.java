package me.libreh.shieldstun.util;

import me.libreh.shieldstun.ShieldStun;

public class Util {
    public static String formatPermission(String key) {
        return "%s.%s".formatted(ShieldStun.MOD_ID, key);
    }
}
