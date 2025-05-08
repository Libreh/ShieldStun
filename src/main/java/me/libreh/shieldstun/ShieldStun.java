package me.libreh.shieldstun;

import me.libreh.shieldstun.command.ShieldStunCommand;
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
		ConfigManager.loadConfig();

		CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> ShieldStunCommand.register(dispatcher));
	}
}