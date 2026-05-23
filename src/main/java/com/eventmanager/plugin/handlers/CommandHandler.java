package com.eventmanager.plugin.handlers;

import com.eventmanager.plugin.EventManager;
import com.eventmanager.plugin.managers.GuiManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private final EventManager plugin;

    public CommandHandler(EventManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("manager.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission for this command.");
            return true;
        }

        plugin.getGuiManager().openGui(player);
        return true;
    }
}
