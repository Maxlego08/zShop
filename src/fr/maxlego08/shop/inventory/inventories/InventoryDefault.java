package fr.maxlego08.shop.inventory.inventories;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryDefault extends VInventory {

	private Inventory inventory;
	private Inventory oldInventory;

	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		if (args.length != 2)
			throw new InventoryOpenException("Pas assez d'argument pour ouvrir l'inventaire");

		inventory = (Inventory) args[0];
		oldInventory = (Inventory) args[1];

		List<Button> buttons = inventory.sortButtons(page);

		createInventory(inventory.getName(), inventory.size());

		for (Button button : buttons)
			addItem(button.getTmpSlot(), button.getItemStack()).setClick(clickEvent(main, player, page, button));

		return InventoryResult.SUCCESS;
	}

	@Override
	public void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {

	}

	@Override
	public void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {

	}

	private Consumer<InventoryClickEvent> clickEvent(ZShop main, Player player, int page, Button button) {
		return event -> {
			switch (button.getType()) {
			case NEXT:
				createInventory(player, EnumInventory.INVENTORY_DEFAULT, page + 1, inventory, oldInventory);
				break;
			case PREVIOUS:
				createInventory(player, EnumInventory.INVENTORY_DEFAULT, page - 1, inventory, oldInventory);
				break;

			default:
				break;
			}
		};
	}

}
