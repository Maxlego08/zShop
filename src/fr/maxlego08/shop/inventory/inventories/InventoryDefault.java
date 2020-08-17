package fr.maxlego08.shop.inventory.inventories;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.buttons.BackButton;
import fr.maxlego08.shop.api.button.buttons.HomeButton;
import fr.maxlego08.shop.api.button.buttons.InventoryButton;
import fr.maxlego08.shop.api.button.buttons.PermissibleButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryDefault extends VInventory {

	private Inventory inventory;
	private Inventory oldInventory;
	private Command command;

	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		if (args.length != 3)
			throw new InventoryOpenException("Pas assez d'argument pour ouvrir l'inventaire");

		inventory = (Inventory) args[0];
		oldInventory = (Inventory) args[1];
		command = (Command) args[2];

		int maxPage = inventory.getMaxPage();

		inventory.getButtons(BackButton.class).forEach(button -> {
			button.setBackInventory(oldInventory == null ? inventory : oldInventory);
		});
		inventory.getButtons(HomeButton.class).forEach(button -> button.setBackInventory(command.getInventory()));

		List<PermissibleButton> buttons = inventory.sortButtons(page);

		// Gestion du nom de l'inventaire
		String inventoryName = inventory.getName();
		inventoryName = inventoryName.replace("%page%", String.valueOf(page));
		inventoryName = inventoryName.replace("%maxPage%", String.valueOf(maxPage));

		createInventory(inventoryName, inventory.size());

		for (PermissibleButton button : buttons) {

			if (button.hasPermission()) {

				if (!player.hasPermission(button.getPermission()) && button.hasElseButton()) {

					addItem(button.getTmpSlot(), button.getElseButton().getItemStack())
							.setClick(clickEvent(main, player, page, maxPage, button));

				} else

					addItem(button.getTmpSlot(), button.getItemStack())
							.setClick(clickEvent(main, player, page, maxPage, button));

			} else

				addItem(button.getTmpSlot(), button.getItemStack())
						.setClick(clickEvent(main, player, page, maxPage, button));

		}

		return InventoryResult.SUCCESS;
	}

	@Override
	public void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {

	}

	@Override
	public void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {

	}

	/**
	 * 
	 * @param main
	 * @param player
	 * @param page
	 * @param maxPage
	 * @param button
	 * @return
	 */
	private Consumer<InventoryClickEvent> clickEvent(ZShop plugin, Player player, int page, int maxPage,
			PermissibleButton button) {
		return event -> {

			PermissibleButton finalButton = button;

			if (finalButton.hasPermission()) {

				if (!player.hasPermission(finalButton.getPermission())) {

					if (finalButton.hasMessage())
						message(player, finalButton.getMessage());

					if (button.hasElseButton())
						finalButton = finalButton.getElseButton().toButton(PermissibleButton.class);
					else
						return;

				}
			}
			
			switch (finalButton.getType()) {
			case NEXT:
				if (page != maxPage)
					createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, page + 1, inventory, oldInventory,
							command);
				break;
			case PREVIOUS:
				if (page != 1)
					createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, page - 1, inventory, oldInventory,
							command);
				break;
			case INVENTORY:
			case HOME:
			case BACK:
				InventoryButton inventoryButton = finalButton.toButton(InventoryButton.class);
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, inventoryButton.getInventory(),
						inventory, command);
				break;
			default:
				break;
			}
		};
	}

}
