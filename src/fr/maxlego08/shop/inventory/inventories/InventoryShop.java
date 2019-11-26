package fr.maxlego08.shop.inventory.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.inventory.ItemButton;
import fr.maxlego08.shop.inventory.VInventory;
import fr.maxlego08.shop.save.inventory.ConfigDefaultInventory;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryShop extends VInventory {

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

		createInventory(ConfigDefaultInventory.inventoryName, ConfigDefaultInventory.inventorySize);

		ConfigDefaultInventory.decoration.forEach((slot, button) -> addItem(slot, button.getInitButton()));

		main.getCategories().getCategories().forEach((id, category) -> {
			if (category.isLoaded() && category.getPermission() == null
					|| player.hasPermission(category.getPermission()))
				addItem(category.getSlot(), new ItemButton(category.toItemStack()).setClick(event -> {
					main.getShop().openShop(player, EnumCategory.SHOP, 1, category);
				}));
		});

		return true;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {

	}

	@Override
	public VInventory clone() {
		return new InventoryShop();
	}

}
