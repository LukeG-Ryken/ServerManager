package com.servermanager.plugin.handlers;

import com.servermanager.plugin.ServerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private final ServerManager plugin;

    public CommandHandler(ServerManager plugin) {
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
