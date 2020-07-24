package me.thevipershow.poisonmodifier.commands;

import me.thevipershow.poisonmodifier.config.Values;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public final class PoisonModifierCommand implements CommandExecutor {
    private final Values values;
    private final Plugin plugin;

    public PoisonModifierCommand(final Values values, final Plugin plugin) {
        this.values = values;
        this.plugin = plugin;
    }

    private static long time() {
        return System.currentTimeMillis();
    }

    private static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private static void sendHelp(final CommandSender sender) {
        sender.sendMessage(color("&8[&aPoisonModifier&8]&7 commands list:&r"));
        sender.sendMessage(color("&7Reload your config.yml file.&r"));
        sender.sendMessage(color("&8- &a/poison-modifier reload&r"));
        sender.sendMessage(color("&7----------------------------&r"));
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final int count = args.length;
        boolean returnValue = false;
        if (count == 0) {
            sendHelp(sender);
            returnValue = true;
        } else if (count == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(color("&8[&aPoisonModifier&8]&7: Updating config.yml values . . ."));
                final long then = time();
                values.updateValues();
                final long taken = time() - then;
                sender.sendMessage(color(String.format("&8[&aPoisonModifier&8]&7: Operation finished in &a%d &7(ms).", taken)));
                returnValue = true;
            }
        }
        return returnValue;
    }
}
