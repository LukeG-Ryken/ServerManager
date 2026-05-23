package com.eventmanager.plugin.managers;

import com.eventmanager.plugin.EventManager;
import com.eventmanager.plugin.managers.StateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.inventoryaccess.component.ComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.ItemWrapper;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class GuiManager {

    private final EventManager plugin;

    public GuiManager(EventManager plugin) {
        this.plugin = plugin;
    }

    public void openGui(Player player) {
        StateManager state = plugin.getStateManager();

        // Layout: 9 slots — fillers around 5 toggle buttons
        // Slot indices: 0=filler, 1=PVP, 2=PVE, 3=Freeze, 4=Nether, 5=End, 6-8=filler
        Gui gui = Gui.normal()
                .setStructure("# P V F N E #")
                .addIngredient('#', new xyz.xenondevs.invui.item.impl.SimpleItem(
                        new ItemWrapper(filler())))
                .addIngredient('P', new ToggleButton(
                        new ItemWrapper(statusItem(Material.DIAMOND_SWORD,  "PVP",    state.isPvp())),
                        plugin, p -> state.togglePvp()))
                .addIngredient('V', new ToggleButton(
                        new ItemWrapper(statusItem(Material.PIG_SPAWN_EGG, "PVE",    state.isPve())),
                        plugin, p -> state.togglePve()))
                .addIngredient('F', new ToggleButton(
                        new ItemWrapper(statusItem(Material.BLUE_ICE,       "Freeze", state.isFrozen())),
                        plugin, p -> state.toggleFrozen()))
                .addIngredient('N', new ToggleButton(
                        new ItemWrapper(statusItem(Material.NETHERRACK,     "Nether", state.isNether())),
                        plugin, p -> state.toggleNether()))
                .addIngredient('E', new ToggleButton(
                        new ItemWrapper(statusItem(Material.END_STONE,      "End",    state.isEnd())),
                        plugin, p -> state.toggleEnd()))
                .build();

        Window.single()
                .setViewer(player)
                .setTitle((ComponentWrapper) Component.text("Event Manager", NamedTextColor.DARK_AQUA))
                .setGui(gui)
                .build()
                .open();
    }

    private ItemStack statusItem(Material material, String label, boolean enabled) {
        ItemStack item = new ItemStack(enabled ? material : Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(label, enabled ? NamedTextColor.GREEN : NamedTextColor.RED));
        meta.lore(List.of(
                Component.text("Status: ", NamedTextColor.GRAY)
                        .append(Component.text(enabled ? "Enabled" : "Disabled",
                                enabled ? NamedTextColor.GREEN : NamedTextColor.RED)),
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
