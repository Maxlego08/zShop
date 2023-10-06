package fr.maxlego08.zshop.buttons;

import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.buttons.ConfirmSellButton;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class ZConfirmSellButton extends ZButton implements ConfirmSellButton {

    private final ShopPlugin plugin;

    public ZConfirmSellButton(Plugin plugin) {
        this.plugin = (ShopPlugin) plugin;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot) {
        super.onClick(player, event, inventory, slot);

        PlayerCache cache = this.plugin.getShopManager().getCache(player);
        ItemButton itemButton = cache.getItemButton();
        itemButton.sell(player, cache.getAmount());
    }
}
