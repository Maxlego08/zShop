package fr.maxlego08.shop.inventory.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.inventory.ItemButton;
import fr.maxlego08.shop.inventory.VInventory;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.inventories.InventoryObject;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryShop extends VInventory {

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

		InventoryObject object = getInventoryObject();

		createInventory(object.getName(), object.getInventorySize());

		object.getDecorations().forEach((slot, button) -> addItem(slot, button.getInitButton()));

		object.getCategories().forEach(category -> {
			if (category.isLoaded())
				addItem(category.getSlot(), new ItemButton(category.getData()).setClick(event -> {
					main.getShop().openShop(player, EnumCategory.SHOP, 1, object.getId(),
							Permission.SHOP_OPEN.getPermission(category.getId()), category);
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
