package me.libreh.shieldstun.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import me.libreh.shieldstun.ShieldStun;
import me.libreh.shieldstun.config.ConfigManager;
import me.libreh.shieldstun.util.Constants;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    private static final int OP_LEVEL = 3;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
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
                                                .executes(context -> configSet(context.getSource(), BoolArgumentType.getBool(context, "value"))))))
                        .then(literal("show")
                                .executes(context -> showAllConfig(context.getSource())))));
    }

    private static int configGet(ServerCommandSource source) {
        sendSuccess(source, Text.literal("%s: ".formatted(Constants.ENABLE_STUNS))
                .append(getFormattedValue(ConfigManager.getConfig().enableStuns)));
        return Command.SINGLE_SUCCESS;
    }

    private static int configSet(ServerCommandSource source, boolean value) {
        ConfigManager.getConfig().enableStuns = value;

        if (ConfigManager.saveConfig()) {
            sendSuccess(source,
                    Text.literal("Set %s to ".formatted(Constants.ENABLE_STUNS))
                            .append(getFormattedValue(ConfigManager.getConfig().enableStuns)));
        } else {
            sendError(source, "Error occurred while reloading config!");
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int showAllConfig(ServerCommandSource source) {
        MutableText message = Text.literal("ShieldStun Config\n")
                .styled(style -> style.withBold(true).withColor(Formatting.GOLD));

        message.append(Text.literal(Constants.ENABLE_STUNS + ": ").formatted(Formatting.YELLOW).styled(style -> style.withBold(false)))
                .append(getFormattedValue(ConfigManager.getConfig().enableStuns).styled(style -> style.withBold(false)));

        source.sendFeedback(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadConfig(ServerCommandSource source) {
        if (ConfigManager.loadConfig()) {
            sendSuccess(source, Text.literal("Reloaded config!"));
        } else {
            sendError(source, "Error occurred while reloading config!");
        }
        return Command.SINGLE_SUCCESS;
    }

    private static MutableText getFormattedValue(Object value) {
        if (value instanceof Boolean) {
            return Text.literal(String.valueOf(value))
                    .formatted((Boolean) value ? Formatting.GREEN : Formatting.RED);
        }
        return Text.literal(String.valueOf(value)).formatted(Formatting.WHITE);
    }

    private static void sendSuccess(ServerCommandSource source, Text text) {
        source.sendFeedback(() -> text, false);
    }

    private static void sendError(ServerCommandSource source, String message) {
        source.sendError(Text.literal(message).formatted(Formatting.RED));
    }
}