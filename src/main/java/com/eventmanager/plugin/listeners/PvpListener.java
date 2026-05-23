package com.eventmanager.plugin.listeners;

import com.eventmanager.plugin.EventManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PvpListener implements Listener {

    private final EventManager plugin;

    public PvpListener(EventManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity victim  = event.getEntity();
        Entity damager = event.getDamager();

        // Resolve actual attacker through projectiles
        if (damager instanceof Projectile projectile) {
            ProjectileSource source = projectile.getShooter();
            if (source instanceof Entity) {
                damager = (Entity) source;
            }
        }

        boolean attackerIsPlayer = damager instanceof Player;
        boolean victimIsPlayer   = victim instanceof Player;

        if (attackerIsPlayer && victimIsPlayer) {
            // PVP
            Player attackerPlayer = (Player) damager;
            if (attackerPlayer.hasPermission("manager.bypass")) return;
            if (plugin.getStateManager().isPvp()) {
                event.setCancelled(true);
            }
        } else if (attackerIsPlayer) {
            // PVE — player attacking mob
            Player attackerPlayer = (Player) damager;
            if (attackerPlayer.hasPermission("manager.bypass")) return;
            if (plugin.getStateManager().isPve()) {
                event.setCancelled(true);
            }
        } else if (victimIsPlayer) {
            // PVE — mob attacking player
            Player victimPlayer = (Player) victim;
            if (victimPlayer.hasPermission("manager.bypass")) return;
            if (plugin.getStateManager().isPve()) {
                event.setCancelled(true);
            }
        }
    }
}
