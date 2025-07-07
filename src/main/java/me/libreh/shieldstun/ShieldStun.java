package me.libreh.shieldstun;

import me.libreh.shieldstun.command.Commands;
import me.libreh.shieldstun.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShieldStun implements ModInitializer {
	public static final String MOD_ID = "shieldstun";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
	public void onInitialize() {
		ConfigManager.getInstance().loadConfig();
		CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> Commands.register(dispatcher));
	}
}