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

    // Slot indices for the 5 toggles (in an 18-slot / 2-row inventory)
    // Row 0 (top):    slots 0-8  — icon + label
    // Row 1 (bottom): slots 9-17 — red/green status
    // Toggles at columns 2,3,4,5,6
    private static final int[] TOGGLE_COLS = {2, 3, 4, 5, 6};

    public GuiManager(ServerManager plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openGui(Player player) {
        StateManager state = plugin.getStateManager();

        Inventory inv = Bukkit.createInventory(null, 18,
                Component.text("Event Manager", NamedTextColor.DARK_AQUA));

        // Fillers
        for (int i = 0; i < 18; i++) inv.setItem(i, filler());

        // Top row — icon + description
        inv.setItem(2, iconItem(Material.NETHERITE_SWORD, "PVP",    "Toggles player vs player damage"));
        inv.setItem(3, iconItem(Material.COW_SPAWN_EGG,   "PVE",    "Toggles player vs environment damage"));
        inv.setItem(4, iconItem(Material.PACKED_ICE,       "Freeze", "Freezes all players in place"));
        inv.setItem(5, iconItem(Material.NETHERRACK,       "Nether", "Toggles access to the Nether"));
        inv.setItem(6, iconItem(Material.CHORUS_FRUIT,     "End",    "Toggles access to the End"));

        // Bottom row — green/red toggle button
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

        event.setCancelled(true); // always cancel — prevents taking items

        StateManager state = plugin.getStateManager();

        // Only respond to clicks on the bottom row toggle buttons
        switch (event.getSlot()) {
            case 11 -> state.togglePvp();
            case 12 -> state.togglePve();
            case 13 -> state.toggleFrozen();
            case 14 -> state.toggleNether();
            case 15 -> state.toggleEnd();
            default -> { return; }
        }

        openGui(player); // reopen with updated state
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
        // GREEN_STAINED_GLASS_PANE when on, RED when off
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