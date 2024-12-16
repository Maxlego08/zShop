package fr.maxlego08.zshop.buttons;

import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.buttons.ShowItemButton;
import fr.maxlego08.zshop.save.ConfirmAction;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfirmationButton extends ZButton {

    private Inventory inventory;

    protected void action(Player player, InventoryDefault inventory, ConfirmAction confirmAction, ShopPlugin plugin, PlayerCache cache) {
        switch (confirmAction) {
            case CLOSE:
                player.closeInventory();
                break;
            case OPEN_BACK:
                if (this.inventory == null) return;

                List<Inventory> oldInventories = inventory.getOldInventories();
                oldInventories.remove(this.inventory);

                Inventory toInventory = this.inventory;
                plugin.getIManager().openInventory(player, toInventory, 1, oldInventories);
                break;
            case RESET_AMOUNT:
                cache.setItemAmount(1);
                inventory.getButtons().stream().filter(button -> button instanceof ShowItemButton).forEach(inventory::buildButton);
                break;
        }
    }

    @Override
    public void onInventoryOpen(Player player, InventoryDefault inventory) {
        List<Inventory> oldInventories = inventory.getOldInventories();
        if (!oldInventories.isEmpty()) {
            this.inventory = oldInventories.get(oldInventories.size() - 1);
        }
    }
}
