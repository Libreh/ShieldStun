package me.libreh.shieldstun.util;

import me.libreh.shieldstun.ModInit;

public class Utils {
    public static String formatPermission(String key) {
        return "%s.%s".formatted(ModInit.ID, key);
    }
}
