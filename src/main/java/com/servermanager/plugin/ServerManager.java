package com.servermanager.plugin;

import com.servermanager.plugin.handlers.CommandHandler;
import com.servermanager.plugin.handlers.TabHandler;
import com.servermanager.plugin.managers.StateManager;
import com.servermanager.plugin.managers.GuiManager;
import com.servermanager.plugin.listeners.PortalListener;
import com.servermanager.plugin.listeners.FreezeListener;
import com.servermanager.plugin.listeners.PvpListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerManager extends JavaPlugin {

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

        getLogger().info("ServerManager has been Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerManager has been Disabled");
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }
}
