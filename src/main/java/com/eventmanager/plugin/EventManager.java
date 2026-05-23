package com.eventmanager.plugin;

import com.eventmanager.plugin.handlers.CommandHandler;
import com.eventmanager.plugin.handlers.TabHandler;
import com.eventmanager.plugin.managers.StateManager;
import com.eventmanager.plugin.managers.GuiManager;
import com.eventmanager.plugin.listeners.PortalListener;
import com.eventmanager.plugin.listeners.FreezeListener;
import com.eventmanager.plugin.listeners.PvpListener;
import org.bukkit.plugin.java.JavaPlugin;

public class EventManager extends JavaPlugin {

    private StateManager stateManager;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        stateManager = new StateManager(this);
        guiManager = new GuiManager(this);

        getCommand("Manager").setExecutor(new CommandHandler(this));
        getCommand("Manager").setTabCompleter(new TabHandler());

        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new FreezeListener(this), this);
        getServer().getPluginManager().registerEvents(new PvpListener(this), this);

        getLogger().info("EventManager has been Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("EventManager has been Disabled");
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }
}
