package me.libreh.shieldstun.config;

import com.google.gson.annotations.SerializedName;
import me.libreh.shieldstun.util.Constants;

public class Config {
    public static final Config DEFAULT = new Config();

    @SerializedName(Constants.ENABLE_STUNS)
    public boolean enableStuns;
}
