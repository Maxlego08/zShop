package fr.maxlego08.shop.inventory.inventories;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryDefault extends VInventory {

	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		if (args.length != 2)
			throw new InventoryOpenException("Pas assez d'argument pour ouvrir l'inventaire");

		Inventory inventory = (Inventory) args[0];
		Inventory oldInventory = (Inventory) args[1];

		List<Button> buttons = inventory.sortButtons(page);

		createInventory(inventory.getName(), inventory.size());
		
		System.out.println(buttons);
		
		for (Button button : buttons) 
			addItem(button.getTmpSlot(), button.getItemStack());

		return InventoryResult.SUCCESS;
	}

	@Override
	public void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {

	}

	@Override
	public void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {

	}

}
