package me.libreh.shieldstun.util;

import me.libreh.shieldstun.ModInit;

public class Util {
    public static String formatPermission(String key) {
        return "%s.%s".formatted(ModInit.ID, key);
    }
}
