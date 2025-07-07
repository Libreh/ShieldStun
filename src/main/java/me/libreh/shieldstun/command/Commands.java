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
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("shieldstun")
                .requires(source -> Permissions.check(source, "shieldstun", 3))
                .then(literal("reload")
                        .requires(source -> Permissions.check(source, "shieldstun.reload", 3))
                        .executes(context -> reloadConfig(context.getSource()))
                        .build()));
    }

    private static int reloadConfig(ServerCommandSource source) {
        if (ConfigManager.getInstance().loadConfig()) {
            source.sendFeedback(() -> Text.literal("Reloaded config!"), false);
        } else {
            source.sendError(Text.literal("Failed to reload the config!").formatted(Formatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}