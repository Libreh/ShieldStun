package me.libreh.shieldstun.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.libreh.shieldstun.config.ConfigManager;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.PermissionLevel;

import static net.minecraft.commands.Commands.literal;

public class Commands {
    private static final Component STUNS_ARE = Component.literal("Stuns are ");
    private static final Component STUNS_HAVE = Component.literal("Stuns have been ");
    private static final Component ENABLED = Component.literal("enabled").withStyle(ChatFormatting.GREEN);
    private static final Component DISABLED = Component.literal("disabled").withStyle(ChatFormatting.RED);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("shieldstun")
                .then(literal("reload")
                        .requires(source -> Permissions.check(source, "shieldstun.reload", PermissionLevel.ADMINS))
                        .executes(context -> reloadConfig(context.getSource())))
                .then(literal("status")
                        .requires(source -> Permissions.check(source, "shieldstun.status", true))
                        .executes(context -> stunStatus(context.getSource())))
                .then(literal("enable")
                        .requires(source -> Permissions.check(source, "shieldstun.enable", PermissionLevel.ADMINS))
                        .executes(context -> enableStuns(context.getSource())))
                .then(literal("disable")
                        .requires(source -> Permissions.check(source, "shieldstun.disable", PermissionLevel.ADMINS))
                        .executes(context -> disableStuns(context.getSource()))));
    }

    private static int reloadConfig(CommandSourceStack source) {
        if (ConfigManager.load()) {
            source.sendSuccess(() -> Component.literal("Reloaded config!"), false);
        } else {
            source.sendFailure(Component.literal("Failed to reload the config! Check server console for more info.").withStyle(ChatFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int stunStatus(CommandSourceStack source) {
        Component status;
        if (ConfigManager.getConfig().enableStuns) {
            status = ENABLED.copy();
        } else {
            status = DISABLED.copy();
        }
        Component message = STUNS_ARE.copy().append(status);
        source.sendSuccess(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int enableStuns(CommandSourceStack source) {
        ConfigManager.getConfig().enableStuns = true;
        ConfigManager.save();
        Component message = STUNS_HAVE.copy().append(ENABLED.copy());
        source.sendSuccess(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int disableStuns(CommandSourceStack source) {
        ConfigManager.getConfig().enableStuns = false;
        ConfigManager.save();
        Component message = STUNS_HAVE.copy().append(DISABLED.copy());
        source.sendSuccess(() -> message, false);
        return Command.SINGLE_SUCCESS;
    }
}