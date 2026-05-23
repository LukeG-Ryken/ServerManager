package com.servermanager.plugin.managers;

import com.servermanager.plugin.ServerManager;
import com.sun.security.ntlm.Server;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class ToggleButton extends AbstractItem {

    private final ItemProvider itemProvider;
    private final ServerManager plugin;
    private final Consumer<Player> onClick;

    public ToggleButton(ItemProvider itemProvider, ServerManager plugin, Consumer<Player> onClick) {
        this.itemProvider = itemProvider;
        this.plugin = plugin;
        this.onClick = onClick;
    }

    @Override
    public ItemProvider getItemProvider() {
        return itemProvider;
    }

    @Override
    public void handleClick(ClickType clickType, Player player, InventoryClickEvent event) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        onClick.accept(player);
        // Rebuild and reopen the GUI so the button state refreshes
        plugin.getGuiManager().openGui(player);
    }
}
