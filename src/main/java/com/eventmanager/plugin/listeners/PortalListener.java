package com.eventmanager.plugin.listeners;

import com.eventmanager.plugin.EventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PortalListener implements Listener {

    private final EventManager plugin;

    public PortalListener(EventManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("manager.bypass")) return;

        TeleportCause cause = event.getCause();

        if (cause == TeleportCause.NETHER_PORTAL && plugin.getStateManager().isNether()) {
            event.setCancelled(true);
        } else if (cause == TeleportCause.END_PORTAL && plugin.getStateManager().isEnd()) {
            event.setCancelled(true);
        }
    }
}
