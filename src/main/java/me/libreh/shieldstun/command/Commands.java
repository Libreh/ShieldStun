package me.libreh.shieldstun.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import me.libreh.shieldstun.ShieldStun;
import me.libreh.shieldstun.config.ConfigManager;
import me.libreh.shieldstun.util.Constants;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    private static final int OP_LEVEL = 3;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(ShieldStun.MOD_ID)
                .requires(source -> Permissions.check(source, Constants.MAIN_PERMISSION, OP_LEVEL))
                .then(literal("reload")
                        .requires(source -> Permissions.check(source, Constants.RELOAD_PERMISSION, OP_LEVEL))
                        .executes(context -> reloadConfig(context.getSource()))
                        .build())
                .then(literal("config")
                        .then(literal("get")
                                .then(literal(Constants.ENABLE_STUNS)
                                .executes(context -> configGet(context.getSource()))))
                        .then(literal("set")
                                .then(literal(Constants.ENABLE_STUNS)
                                        .then(argument("value", BoolArgumentType.bool())
                                                .executes(context -> configSet(context.getSource(), BoolArgumentType.getBool(context, "value"))))))));
    }

    private static int configGet(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("%s: ".formatted(Constants.ENABLE_STUNS))
                        .append(formatBoolean(ConfigManager.getConfig().enableStuns)),
                false);
        return Command.SINGLE_SUCCESS;
    }

    private static int configSet(ServerCommandSource source, boolean value) {
        ConfigManager.getConfig().enableStuns = value;

        if (ConfigManager.saveConfig()) {
            source.sendFeedback(() -> Text.literal("Set %s to ".formatted(Constants.ENABLE_STUNS))
                            .append(formatBoolean(ConfigManager.getConfig().enableStuns)),
                    false);
        } else {
            source.sendError(Text.literal("Error occurred while saving config!").formatted(Formatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadConfig(ServerCommandSource source) {
        if (ConfigManager.loadConfig()) {
            source.sendFeedback(() -> Text.literal("Reloaded config!"), false);
        } else {
            source.sendError(Text.literal("Error occurred while reloading config!").formatted(Formatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static MutableText formatBoolean(boolean value) {
        return Text.literal(String.valueOf(value)).formatted(value ? Formatting.GREEN : Formatting.RED);
    }
}