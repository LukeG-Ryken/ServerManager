package com.servermanager.plugin.listeners;

import com.servermanager.plugin.ServerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {

    private final ServerManager plugin;

    public FreezeListener(ServerManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("manager.bypass") || player.isOp()) return;
        if (!plugin.getStateManager().isFrozen()) return;

        Location from = event.getFrom();
        Location to   = event.getTo();
        if (to == null) return;

        // Cancel lateral movement; allow looking around
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.hasPermission("manager.bypass")) return;
        if (!plugin.getStateManager().isFrozen()) return;
        event.setCancelled(true);
    }
}
