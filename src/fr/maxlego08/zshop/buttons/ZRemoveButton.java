package fr.maxlego08.zshop.buttons;

import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.buttons.AddButton;
import fr.maxlego08.zshop.api.buttons.ShowItemButton;
import fr.maxlego08.zshop.placeholder.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ZRemoveButton extends ZButton implements AddButton {

    private final ShopPlugin plugin;
    private final String amount;

    public ZRemoveButton(ShopPlugin plugin, String amount) {
        this.plugin = plugin;
        this.amount = amount;
    }

    @Override
    public String getAmount() {
        return this.amount;
    }

    @Override
    public int parseInt(Player player) {
        int amount = 1;
        try {
            amount = Integer.parseInt(Placeholder.getPlaceholder().setPlaceholders(player, this.amount));
        } catch (Exception ignored) {
        }
        return amount;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot) {
        super.onClick(player, event, inventory, slot);

        int amount = parseInt(player);
        PlayerCache cache = this.plugin.getShopManager().getCache(player);
        cache.setItemAmount(Math.max(cache.getAmount() - amount, 1));

        inventory.getButtons().stream().filter(button -> button instanceof ShowItemButton).forEach(inventory::buildButton);
    }
}
