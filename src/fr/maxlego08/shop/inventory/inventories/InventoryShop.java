package fr.maxlego08.shop.inventory.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.inventory.VInventory;

public class InventoryShop extends VInventory {

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

//		createInventory(ConfigDefaultInventory.inventoryName, ConfigDefaultInventory.inventorySize);
//
//		ConfigDefaultInventory.decoration.forEach((slot, button) -> addItem(slot, button.getInitButton()));
//
//		main.getCategories().getCategories().forEach((id, category) -> {
//			if (category.isLoaded())
//				addItem(category.getSlot(), new ItemButton(category.getData()).setClick(event -> {
//					main.getShop().openShop(player, EnumCategory.SHOP, 1,
//							Permission.SHOP_OPEN.getPermission(category.getId()), category);
//				}));
//		});

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
