package com.servermanager.plugin.managers;

import com.servermanager.plugin.ServerManager;
import org.bukkit.configuration.file.FileConfiguration;

public class StateManager {

    private final ServerManager plugin;

    // Keys in config
    private static final String PVP    = "Manager.pvp";
    private static final String PVE    = "Manager.pve";
    private static final String FROZEN = "Manager.frozen";
    private static final String NETHER = "Manager.nether";
    private static final String END    = "Manager.end";

    public StateManager(ServerManager plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration cfg() {
        return plugin.getConfig();
    }

    public boolean isPvp()    { return cfg().getBoolean(PVP, false); }
    public boolean isPve()    { return cfg().getBoolean(PVE, false); }
    public boolean isFrozen() { return cfg().getBoolean(FROZEN, false); }
    public boolean isNether() { return cfg().getBoolean(NETHER, false); }
    public boolean isEnd()    { return cfg().getBoolean(END, false); }

    public boolean toggle(String key) {
        boolean current = cfg().getBoolean(key, false);
        cfg().set(key, !current);
        plugin.saveConfig();
        return !current;
    }

    public boolean togglePvp()    { return toggle(PVP); }
    public boolean togglePve()    { return toggle(PVE); }
    public boolean toggleFrozen() { return toggle(FROZEN); }
    public boolean toggleNether() { return toggle(NETHER); }
    public boolean toggleEnd()    { return toggle(END); }
}
