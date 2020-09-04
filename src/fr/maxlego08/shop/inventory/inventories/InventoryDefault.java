package fr.maxlego08.shop.inventory.inventories;

import java.util.ArrayList;
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
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.PerformButton;
import fr.maxlego08.shop.api.button.buttons.PermissibleButton;
import fr.maxlego08.shop.api.button.buttons.PlaceholderButton;
import fr.maxlego08.shop.api.button.buttons.SlotButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.exceptions.InventoryTypeException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;
import fr.maxlego08.shop.zcore.utils.inventory.ZButton;

public class InventoryDefault extends VInventory {

	private Inventory inventory;
	private List<Inventory> oldInventories;
	private Command command;

	@SuppressWarnings("unchecked")
	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		if (args.length < 3)
			throw new InventoryOpenException("Pas assez d'argument pour ouvrir l'inventaire");

		inventory = (Inventory) args[0];

		if (!inventory.getType().isDefault())
			throw new InventoryTypeException("Cannot open default inventory with type " + inventory.getType());

		oldInventories = (List<Inventory>) args[1];
		command = (Command) args[2];

		int maxPage = inventory.getMaxPage();

		int size = oldInventories.size() - 1;

		if (size >= 0) {
			Inventory oldInventory = oldInventories.get(size);

			inventory.getButtons(BackButton.class).forEach(button -> {
				button.setBackInventory(oldInventory == null ? inventory : oldInventory);
			});
		}

		inventory.getButtons(HomeButton.class).forEach(button -> button.setBackInventory(command.getInventory()));

		List<PlaceholderButton> buttons = inventory.sortButtons(page);

		// Gestion du nom de l'inventaire
		String inventoryName = inventory.getName();
		inventoryName = inventoryName.replace("%page%", String.valueOf(page));
		inventoryName = inventoryName.replace("%maxPage%", String.valueOf(maxPage));

		createInventory(papi(inventoryName, player), inventory.size());

		for (PlaceholderButton button : buttons) {

			if (button.hasPermission()) {

				if (!button.checkPermission(player) && button.hasElseButton()) {

					PlaceholderButton elseButton = button.getElseButton().toButton(PlaceholderButton.class);
					ZButton zButton = addItem(button.getTmpSlot(), elseButton.getCustomItemStack(player));

					if (elseButton.isClickable()) {
						zButton.setClick(clickEvent(main, player, page, maxPage, elseButton));
						if (elseButton.getType().isOtherClick()) {
							zButton.setLeftClick(leftClick(main, player, page, maxPage, elseButton))
									.setRightClick(rightClick(main, player, page, maxPage, elseButton))
									.setMiddleClick(middleClick(main, player, page, maxPage, elseButton));
						}
					}

				} else {

					if (button.getType().isSlots()) {
						button.toButton(SlotButton.class).getSlots().forEach(slot -> {
							addItem(slot, button.getCustomItemStack(player));
						});
					} else {

						ZButton zButton = addItem(button.getTmpSlot(), button.getCustomItemStack(player));
						if (button.isClickable()) {
							zButton.setClick(clickEvent(main, player, page, maxPage, button));
							if (button.getType().isOtherClick()) {
								zButton.setLeftClick(leftClick(main, player, page, maxPage, button))
										.setRightClick(rightClick(main, player, page, maxPage, button))
										.setMiddleClick(middleClick(main, player, page, maxPage, button));
							}
						}
					}
				}
			} else {

				if (button.getType().isSlots()) {

					button.toButton(SlotButton.class).getSlots().forEach(slot -> {
						addItem(slot, button.getCustomItemStack(player));
					});

				} else {

					ZButton zButton = addItem(button.getTmpSlot(), button.getCustomItemStack(player));
					if (button.isClickable()) {
						zButton.setClick(clickEvent(main, player, page, maxPage, button));
						if (button.getType().isOtherClick()) {
							zButton.setLeftClick(leftClick(main, player, page, maxPage, button))
									.setRightClick(rightClick(main, player, page, maxPage, button))
									.setMiddleClick(middleClick(main, player, page, maxPage, button));
						}
					}
				}
			}
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
			PlaceholderButton button) {
		return event -> {

			PlaceholderButton finalButton = button;

			if (finalButton.hasPermission()) {

				if (!button.checkPermission(player)) {

					if (finalButton.hasMessage())
						message(player, finalButton.getMessage());

					if (button.hasElseButton())
						finalButton = finalButton.getElseButton().toButton(PlaceholderButton.class);
					else
						return;

				}
			}

			switch (finalButton.getType()) {
			case NEXT:
				if (page != maxPage)
					createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, page + 1, inventory,
							oldInventories, command);
				break;
			case PREVIOUS:
				if (page != 1)
					createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, page - 1, inventory,
							oldInventories, command);
				break;
			case ITEM_CONFIRM:
			case ZSPAWNER:
				this.oldInventories.add(this.inventory);
				plugin.getShopManager().open(player, this.command, finalButton.toButton(ItemButton.class), page,
						this.oldInventories, InventoryType.CONFIRM);
				break;
			case INVENTORY:
				this.oldInventories.add(inventory);
				InventoryButton inventoryButton = finalButton.toButton(InventoryButton.class);
				Inventory toInventory = inventoryButton.getInventory();

				if (!toInventory.getType().isDefault()) {

					message(player, "§cUnable to navigate to the §f" + toInventory.getName()
							+ "inventory §c, please contact an administrator to correct the problem.");

					Logger.info("Player " + player.getName() + " wanted to go to the " + toInventory.getName()
							+ " inventory but the inventory type is incorrect.", LogType.ERROR);

					return;
				}

				if (args.length == 5) {

					InventoryType type = (InventoryType) args[4];
					ItemButton itemButton = (ItemButton) args[3];

					createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, toInventory, oldInventories,
							command, itemButton, type);
				} else

					createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, toInventory, oldInventories,
							command);
				break;
			case HOME:
				inventoryButton = finalButton.toButton(InventoryButton.class);
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, inventoryButton.getInventory(),
						new ArrayList<>(), command);
				break;
			case PERFORM_COMMAND:
				PerformButton performButton = finalButton.toButton(PerformButton.class);
				performButton.execute(player);
				break;
			case BACK:

				inventoryButton = finalButton.toButton(InventoryButton.class);
				Inventory currentInventory = inventoryButton.getInventory();
				this.oldInventories.remove(currentInventory);

				if (currentInventory.getType().isShop()) {

					InventoryType type = (InventoryType) args[4];
					ItemButton itemButton = (ItemButton) args[3];

					plugin.getShopManager().open(player, command, itemButton, maxPage, oldInventories, type);

				} else {

					if (args.length == 5) {

						InventoryType type = (InventoryType) args[4];
						ItemButton itemButton = (ItemButton) args[3];

						createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, currentInventory,
								oldInventories, command, itemButton, type);
					} else

						createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, currentInventory,
								oldInventories, command);

				}
				break;
			default:
				break;
			}
		};
	}

	private Consumer<InventoryClickEvent> middleClick(ZShop plugin, Player player, int page, int maxPage,
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
			case ITEM:
				ItemButton itemButton = button.toButton(ItemButton.class);
				if (itemButton.canSell())
					itemButton.sell(player, 0);
				break;
			default:
				break;

			}

		};
	}

	private Consumer<InventoryClickEvent> rightClick(ZShop plugin, Player player, int page, int maxPage,
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
			case ITEM:
				ItemButton itemButton = button.toButton(ItemButton.class);
				if (itemButton.canSell()) {

					/*
					 * if (args.length == 5) { message(player,
					 * "§cYou cannot access a shop inventory if you have already opened an inventory of type SELL or BUY. Contact an administrator to correct the problem."
					 * ); player.closeInventory(); return; }
					 */

					this.oldInventories.add(this.inventory);
					plugin.getShopManager().open(player, this.command, itemButton, page, this.oldInventories,
							InventoryType.SELL);
				}
				break;
			default:
				break;

			}
		};
	}

	private Consumer<InventoryClickEvent> leftClick(ZShop plugin, Player player, int page, int maxPage,
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
			case ITEM:
				ItemButton itemButton = button.toButton(ItemButton.class);
				if (itemButton.canBuy()) {

					/*
					 * if (args.length == 5) { message(player,
					 * "§cYou cannot access a shop inventory if you have already opened an inventory of type SELL or BUY. Contact an administrator to correct the problem."
					 * ); player.closeInventory(); return; }
					 */

					this.oldInventories.add(this.inventory);
					plugin.getShopManager().open(player, this.command, itemButton, page, this.oldInventories,
							InventoryType.BUY);
				}
				break;
			default:
				break;

			}
		};
	}

}
