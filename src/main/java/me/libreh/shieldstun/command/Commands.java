package me.libreh.shieldstun.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.libreh.shieldstun.ModInit;
import me.libreh.shieldstun.config.ConfigManager;
import me.libreh.shieldstun.config.ConfigOption;
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
        dispatcher.register(literal(ModInit.ID)
                .requires(source -> Permissions.check(source, Constants.MAIN_PERMISSION, OP_LEVEL))
                .then(createReloadCommand())
                .then(createConfigCommands()));
    }

    private static LiteralCommandNode<ServerCommandSource> createReloadCommand() {
        return literal("reload")
                .requires(source -> Permissions.check(source, Constants.RELOAD_PERMISSION, OP_LEVEL))
                .executes(context -> reloadConfig(context.getSource()))
                .build();
    }

    private static LiteralCommandNode<ServerCommandSource> createConfigCommands() {
        LiteralArgumentBuilder<ServerCommandSource> config = literal("config");

        ConfigManager.getConfig().getOptions().values().forEach(option ->
                config.then(literal(option.getKey())
                        .then(literal("get")
                                .executes(context -> handleConfigGet(context.getSource(), option)))
                        .then(literal("set")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> handleConfigSet(context, option))))));

        config.then(literal("show")
                .executes(context -> showAllConfig(context.getSource())));

        return config.build();
    }

    private static int handleConfigGet(ServerCommandSource source, ConfigOption<?> option) {
        Object value = option.get();
        sendSuccess(source, Text.literal("%s: ".formatted(option.getKey()))
                .append(getFormattedValue(value)));
        return Command.SINGLE_SUCCESS;
    }

    private static int handleConfigSet(CommandContext<ServerCommandSource> context, ConfigOption<?> option) {
        boolean value = BoolArgumentType.getBool(context, "value");
        ((ConfigOption<Boolean>) option).set(value);

        if (ConfigManager.saveConfig()) {
            sendSuccess(context.getSource(),
                    Text.literal("Set %s to ".formatted(option.getKey()))
                            .append(getFormattedValue(value)));
        } else {
            sendError(context.getSource(), "Failed to save config!");
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int showAllConfig(ServerCommandSource source) {
        MutableText message = Text.literal("ShieldStun Config\n")
                .styled(style -> style.withBold(true).withColor(Formatting.GOLD));

        ConfigManager.getConfig().getOptions().values().forEach(option -> {
            Object value = option.get();
            message.append("\n")
                    .append(Text.literal(option.getKey() + ": ").formatted(Formatting.YELLOW).styled(style -> style.withBold(false)))
                    .append(getFormattedValue(value).styled(style -> style.withBold(false)));
        });

        source.sendFeedback(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadConfig(ServerCommandSource source) {
        if (ConfigManager.loadConfig()) {
            sendSuccess(source, Text.literal("Config reloaded successfully!").formatted(Formatting.GREEN));
        } else {
            sendError(source, "Failed to reload config!");
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