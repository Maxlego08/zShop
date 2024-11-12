package fr.maxlego08.zshop.inventory;

import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.zshop.save.Config;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class SellInventoryHolder implements InventoryHolder {

    private final Inventory inventory;

    public SellInventoryHolder(MetaUpdater updater) {
        this.inventory = updater.createInventory(Config.sellInventoryTitle, 45, this);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
