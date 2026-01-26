package me.libreh.shieldstun;

import me.libreh.shieldstun.command.ShieldStunCommand;
import me.libreh.shieldstun.config.ConfigManager;
import me.libreh.shieldstun.util.GenericModInfo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.ChatFormatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShieldStun implements ModInitializer {
	public static final String MOD_ID = "shieldstun";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModContainer CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).get();

    @Override
	public void onInitialize() {
        GenericModInfo.build(CONTAINER, MOD_ID, LOGGER, true, true, ChatFormatting.RED.getColor());

		ConfigManager.load();
		CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> ShieldStunCommand.register(dispatcher));
	}
}