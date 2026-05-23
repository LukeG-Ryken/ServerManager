package com.servermanager.plugin.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabHandler implements TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of(
            "togglePVP", "togglePVE", "toggleFreeze", "toggleNether", "toggleEnd"
    );

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (String sub : SUBCOMMANDS) {
                if (sub.toLowerCase().startsWith(partial)) {
                    completions.add(sub);
                }
            }
        }
        return completions;
    }
}
