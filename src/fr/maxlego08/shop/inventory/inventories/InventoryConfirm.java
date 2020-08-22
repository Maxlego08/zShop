package fr.maxlego08.shop.inventory.inventories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.BackButton;
import fr.maxlego08.shop.api.button.buttons.HomeButton;
import fr.maxlego08.shop.api.button.buttons.InventoryButton;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.ShowButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryConfirm extends VInventory {

	private Inventory inventory;
	private List<Inventory> oldInventories;
	private Command command;
	private ItemButton button;
	private int oldPage;

	@SuppressWarnings("unchecked")
	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		this.inventory = (Inventory) args[0];
		this.button = (ItemButton) args[1];
		this.oldInventories = (List<Inventory>) args[2];
		this.oldPage = (Integer) args[3];
		this.command = (Command) args[4];

		createInventory(inventory.getName(), inventory.size());

		Inventory oldInventory = null;

		int size = oldInventories.size() - 1;

		if (size >= 0)
			oldInventory = oldInventories.get(size);

		final Inventory finalInventory = oldInventory;

		Collection<Button> buttons = inventory.getButtons();
		buttons.forEach(button -> {

			if (button.getType().equals(ButtonType.HOME))
				button.toButton(HomeButton.class).setBackInventory(command.getInventory());

			if (button.getType().equals(ButtonType.BACK) && finalInventory != null)
				button.toButton(BackButton.class).setBackInventory(finalInventory);

			if (button.getType().equals(ButtonType.SHOW_ITEM)) {

				ShowButton showButton = button.toButton(ShowButton.class);
				addItem(button.getSlot(), showButton.applyLore(this.button, 1, InventoryType.CONFIRM));

			} else
				addItem(button.getSlot(), button.getItemStack()).setClick(clickEvent(main, player, page, button));
		});

		return InventoryResult.SUCCESS;
	}

	private Consumer<InventoryClickEvent> clickEvent(ZShop plugin, Player player, int page, Button currentButton) {
		return event -> {
			switch (currentButton.getType()) {
			case BUY_CONFIRM:
				button.buy(player, 1);
				player.closeInventory();
				break;
			case HOME:
				InventoryButton inventoryButton = currentButton.toButton(InventoryButton.class);
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, inventoryButton.getInventory(),
						new ArrayList<>(), command);
			case INVENTORY:

				inventoryButton = currentButton.toButton(InventoryButton.class);
				oldInventories.add(this.inventory);
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, oldPage,
						inventoryButton.getInventory(), this.oldInventories, this.command, this.button,
						InventoryType.CONFIRM);

				break;
			case BACK:
				inventoryButton = currentButton.toButton(InventoryButton.class);
				oldInventories.remove(inventoryButton.getInventory());
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, oldPage,
						inventoryButton.getInventory(), this.oldInventories, this.command, this.button,
						InventoryType.CONFIRM);
				break;
			default:
				break;
			}
		};
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
