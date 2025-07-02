package me.libreh.shieldstun.config;

import com.google.gson.annotations.SerializedName;
import me.libreh.shieldstun.util.Constants;

public class Config {
    @SerializedName(Constants.ENABLE_STUNS)
    public boolean enableStuns = true;
}
