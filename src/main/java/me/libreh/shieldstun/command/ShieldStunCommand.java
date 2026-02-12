package me.libreh.shieldstun.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.libreh.shieldstun.config.ConfigManager;
import me.libreh.shieldstun.util.GenericModInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.literal;

public class ShieldStunCommand {
    private static final int ADMIN_PERMISSION_LEVEL = 3;
    private static final Component STUNS_ARE = Component.literal("Stuns are ");
    private static final Component STUNS_HAVE = Component.literal("Stuns have been ");
    private static final Component ENABLED = Component.literal("enabled").withStyle(ChatFormatting.GREEN);
    private static final Component DISABLED = Component.literal("disabled").withStyle(ChatFormatting.RED);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("shieldstun")
                .executes(ShieldStunCommand::about)
                .then(literal("reload")
                        .requires(source -> source.hasPermission(ADMIN_PERMISSION_LEVEL))
                        .executes(context -> reloadConfig(context.getSource())))
                .then(literal("status")
                        .requires(source -> source.hasPermission(0))
                        .executes(context -> stunStatus(context.getSource())))
                .then(literal("enable")
                        .requires(source -> source.hasPermission(ADMIN_PERMISSION_LEVEL))
                        .executes(context -> enableStuns(context.getSource())))
                .then(literal("disable")
                        .requires(source -> source.hasPermission(ADMIN_PERMISSION_LEVEL))
                        .executes(context -> disableStuns(context.getSource()))));
    }

    private static int about(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        for (var text : source.getEntity() instanceof ServerPlayer ? GenericModInfo.getAboutFull() : GenericModInfo.getAboutConsole()) {
            source.sendSuccess(text, false);
        }

        return 1;
    }

    private static int reloadConfig(CommandSourceStack source) {
        if (ConfigManager.load()) {
            source.sendSuccess(Component.literal("Reloaded config!"), false);
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
        source.sendSuccess(message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int enableStuns(CommandSourceStack source) {
        ConfigManager.getConfig().enableStuns = true;
        ConfigManager.save();
        Component message = STUNS_HAVE.copy().append(ENABLED.copy());
        source.sendSuccess(message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int disableStuns(CommandSourceStack source) {
        ConfigManager.getConfig().enableStuns = false;
        ConfigManager.save();
        Component message = STUNS_HAVE.copy().append(DISABLED.copy());
        source.sendSuccess(message, false);
        return Command.SINGLE_SUCCESS;
    }
}