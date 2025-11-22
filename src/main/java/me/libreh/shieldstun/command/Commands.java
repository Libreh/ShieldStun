package me.libreh.shieldstun.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.libreh.shieldstun.config.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    private static final Text STUNS_ARE = Text.literal("Stuns are ");
    private static final Text STUNS_HAVE = Text.literal("Stuns have been ");
    private static final Text ENABLED = Text.literal("enabled").formatted(Formatting.GREEN);
    private static final Text DISABLED = Text.literal("disabled").formatted(Formatting.RED);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("shieldstun")
                .then(literal("reload")
                        .requires(source -> Permissions.check(source, "shieldstun.reload", 3))
                        .executes(context -> reloadConfig(context.getSource())))
                .then(literal("status")
                        .requires(source -> Permissions.check(source, "shieldstun.status", true))
                        .executes(context -> stunStatus(context.getSource())))
                .then(literal("enable")
                        .requires(source -> Permissions.check(source, "shieldstun.enable", 3))
                        .executes(context -> enableStuns(context.getSource())))
                .then(literal("disable")
                        .requires(source -> Permissions.check(source, "shieldstun.disable", 3))
                        .executes(context -> disableStuns(context.getSource()))));
    }

    private static int reloadConfig(ServerCommandSource source) {
        if (ConfigManager.loadConfig()) {
            source.sendFeedback(() -> Text.literal("Reloaded config!"), false);
        } else {
            source.sendError(Text.literal("Failed to reload the config! Check server console for more info.").formatted(Formatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int stunStatus(ServerCommandSource source) {
        Text status;
        if (ConfigManager.getConfig().enableStuns) {
            status = ENABLED.copy();
        } else {
            status = DISABLED.copy();
        }
        Text message = STUNS_ARE.copy().append(status);
        source.sendFeedback(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int enableStuns(ServerCommandSource source) {
        ConfigManager.getConfig().enableStuns = true;
        ConfigManager.saveConfig();
        Text message = STUNS_HAVE.copy().append(ENABLED.copy());
        source.sendFeedback(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int disableStuns(ServerCommandSource source) {
        ConfigManager.getConfig().enableStuns = false;
        ConfigManager.saveConfig();
        Text message = STUNS_HAVE.copy().append(DISABLED.copy());
        source.sendFeedback(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }
}