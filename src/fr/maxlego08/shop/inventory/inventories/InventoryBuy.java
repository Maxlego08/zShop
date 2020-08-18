package fr.maxlego08.shop.inventory.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.exceptions.InventoryTypeException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryBuy extends VInventory {

	private Inventory inventory;

	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		inventory = (Inventory) args[0];

		if (!inventory.getType().isBuy())
			throw new InventoryTypeException("Cannot open buy inventory with type " + inventory.getType());

		createInventory(inventory.getName(), inventory.size());
		
		inventory.getButtons().forEach(button -> {
			addItem(button.getSlot(), button.getItemStack());
		});

		return InventoryResult.SUCCESS;
	}

	@Override
	public void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {
		// TODO Auto-generated method stub

	}

}
