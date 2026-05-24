package com.servermanager.plugin.managers;

import com.servermanager.plugin.ServerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GuiManager implements Listener {

    private final ServerManager plugin;
    private final Set<UUID> openInventories = new HashSet<>();

    public GuiManager(ServerManager plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openGui(Player player) {
        StateManager state = plugin.getStateManager();

        Inventory inv = Bukkit.createInventory(null, 18,
                Component.text("Event Manager", NamedTextColor.DARK_AQUA));

        for (int i = 0; i < 18; i++) inv.setItem(i, filler());

        // Top row — icon + name + state-aware description
        inv.setItem(2, iconItem(Material.NETHERITE_SWORD, "PVP",
                state.isPvp()
                        ? "PvP is enabled"
                        : "PvP is disabled"));

        inv.setItem(3, iconItem(Material.COW_SPAWN_EGG, "PVE",
                state.isPve()
                        ? "Players can take environment damage"
                        : "Players are safe from the environment"));

        inv.setItem(4, iconItem(Material.PACKED_ICE, "Freeze",
                state.isFrozen()
                        ? "Players are frozen in place"
                        : "Players can move freely"));

        inv.setItem(5, iconItem(Material.NETHERRACK, "Nether",
                state.isNether()
                        ? "The Nether is accessible"
                        : "The Nether is blocked"));

        inv.setItem(6, iconItem(Material.CHORUS_FRUIT, "End",
                state.isEnd()
                        ? "The End is accessible"
                        : "The End is blocked"));

        // Bottom row — green/red toggle directly below each icon
        inv.setItem(11, toggleItem(state.isPvp()));
        inv.setItem(12, toggleItem(state.isPve()));
        inv.setItem(13, toggleItem(state.isFrozen()));
        inv.setItem(14, toggleItem(state.isNether()));
        inv.setItem(15, toggleItem(state.isEnd()));

        openInventories.add(player.getUniqueId());
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!openInventories.contains(player.getUniqueId())) return;
        if (!Component.text("Event Manager", NamedTextColor.DARK_AQUA)
                .equals(event.getView().title())) return;

        event.setCancelled(true);

        StateManager state = plugin.getStateManager();

        switch (event.getSlot()) {
            case 11 -> state.togglePvp();
            case 12 -> state.togglePve();
            case 13 -> state.toggleFrozen();
            case 14 -> state.toggleNether();
            case 15 -> state.toggleEnd();
            default -> { return; }
        }

        openGui(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        openInventories.remove(event.getPlayer().getUniqueId());
    }

    private ItemStack iconItem(Material material, String label, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(label, NamedTextColor.WHITE));
        meta.lore(List.of(
                Component.text(description, NamedTextColor.GRAY)
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack toggleItem(boolean enabled) {
        Material mat = enabled ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(
                enabled
                        ? Component.text("✔ Enabled",  NamedTextColor.GREEN)
                        : Component.text("✘ Disabled", NamedTextColor.RED)
        );
        meta.lore(List.of(
                Component.text("Click to toggle", NamedTextColor.YELLOW)
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack filler() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.empty());
        item.setItemMeta(meta);
        return item;
    }
}