package fr.maxlego08.shop.inventory.inventories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.AddRemoveButton;
import fr.maxlego08.shop.api.button.buttons.BackButton;
import fr.maxlego08.shop.api.button.buttons.HomeButton;
import fr.maxlego08.shop.api.button.buttons.InventoryButton;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.PerformButton;
import fr.maxlego08.shop.api.button.buttons.ShowButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.exceptions.InventoryTypeException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryShop extends VInventory {

	private Inventory inventory;
	private List<Inventory> oldInventories;
	private Command command;
	private ItemButton button;
	private int oldPage;
	private int amount = 1;
	private InventoryType type;
	private List<ShowButton> shows = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		this.inventory = (Inventory) args[0];
		this.button = (ItemButton) args[1];
		this.oldInventories = (List<Inventory>) args[2];
		this.oldPage = (Integer) args[3];
		this.command = (Command) args[4];
		this.type = (InventoryType) args[5];
		this.amount = 1;
		this.shows.clear();

		if (!inventory.getType().isShop())
			throw new InventoryTypeException("Cannot open buy inventory with type " + inventory.getType());

		createInventory(papi(inventory.getName(), player), inventory.size());

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
				addItem(button.getSlot(), showButton.applyLore(this.player, this.button, amount, type));
				this.shows.add(showButton);

			} else if (button.getType().equals(ButtonType.SET_TO_MAX)) {

				// Bouton max

				ItemStack itemStack = button.getItemStack();
				ItemMeta itemMeta = itemStack.getItemMeta();
				if (itemMeta.hasDisplayName())
					itemMeta.setDisplayName(
							itemMeta.getDisplayName().replace("%maxStack%", String.valueOf(this.button.getMaxStack())));
				itemStack.setItemMeta(itemMeta);

				addItem(button.getSlot(), itemStack).setClick(clickEvent(main, player, page, button));

			} else if (button.getType().equals(ButtonType.ADD) || button.getType().equals(ButtonType.REMOVE))
				// Bouton add remove

				addItem(button.getSlot(), button.getItemStack()).setClick(clickEvent(main, player, page, button));

			else // Bouton classique
				addItem(button.getSlot(), button.getItemStack()).setClick(clickEvent(main, player, page, button));

		});

		return InventoryResult.SUCCESS;
	}

	private Consumer<InventoryClickEvent> clickEvent(ZShop plugin, Player player, int page, Button currentButton) {
		currentButton.playSound(player);
		return event -> {
			switch (currentButton.getType()) {
			case BUY_CONFIRM:
				button.buy(player, amount);
				break;
			case SELL_CONFIRM:
				button.sell(player, amount);
				break;
			case ADD:
				AddRemoveButton addRemoveButton = currentButton.toButton(AddRemoveButton.class);
				amount += addRemoveButton.getAmount();
				if (amount > button.getMaxStack())
					amount = button.getMaxStack();
				this.itemRender();
				break;
			case REMOVE:
				addRemoveButton = currentButton.toButton(AddRemoveButton.class);
				amount -= addRemoveButton.getAmount();
				if (amount < 1)
					amount = 1;
				this.itemRender();
				break;
			case SET_TO_MAX:
				amount = this.button.getMaxStack();
				this.itemRender();
				break;
			case SET_TO_ONE:
				amount = 1;
				this.itemRender();
				break;
			case HOME:
				InventoryButton inventoryButton = currentButton.toButton(InventoryButton.class);
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, inventoryButton.getInventory(),
						new ArrayList<>(), command);
			case INVENTORY:
				
				inventoryButton = currentButton.toButton(InventoryButton.class);
				oldInventories.add(this.inventory);
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, oldPage,
						inventoryButton.getInventory(), this.oldInventories, this.command, this.button, this.type);
				
				break;
			case BACK:
				inventoryButton = currentButton.toButton(InventoryButton.class);
				oldInventories.remove(inventoryButton.getInventory());
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, oldPage,
						inventoryButton.getInventory(), this.oldInventories, this.command, this.button, this.type);
				break;
			case PERFORM_COMMAND:
				PerformButton performButton = currentButton.toButton(PerformButton.class);
				performButton.execute(player);
				break;
			default:
				break;
			}
		};
	}

	private void itemRender() {
		this.shows.forEach(showButton -> {
			super.inventory.setItem(showButton.getSlot(), showButton.applyLore(this.player, this.button, amount, type));
		});
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
