package com.servermanager.plugin.handlers;

import com.servermanager.plugin.ServerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return true;
        }

        if (!player.hasPermission("manager.admin")) {
            sender.sendMessage(Component.text("You do not have permission for this command.", NamedTextColor.RED));
            return true;
        }

        plugin.getGuiManager().openGui(player);
        return true;
    }
}
