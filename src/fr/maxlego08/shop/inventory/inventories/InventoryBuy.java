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
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.PermissibleButton;
import fr.maxlego08.shop.api.button.buttons.ShowButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.api.exceptions.InventoryTypeException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryBuy extends VInventory {

	private Inventory inventory;
	private Inventory oldInventory;
	private ItemButton button;
	private int oldPage;
	private int amount = 1;

	@Override
	public InventoryResult openInventory(ZShop main, Player player, int page, Object... args)
			throws InventoryOpenException {

		this.inventory = (Inventory) args[0];
		this.button = (ItemButton) args[1];
		this.oldInventory = (Inventory) args[2];
		this.oldPage = (Integer) args[3];
		this.amount = 1;

		if (!inventory.getType().isBuy())
			throw new InventoryTypeException("Cannot open buy inventory with type " + inventory.getType());

		createInventory(inventory.getName(), inventory.size());

		Collection<Button> buttons = inventory.getButtons();
		buttons.forEach(button -> {

			if (button.getType().equals(ButtonType.SHOW_ITEM)) {

				ItemStack itemStack = this.button.getItemStack().clone();
				List<String> lore = new ArrayList<>();
				ItemMeta itemMeta = itemStack.getItemMeta();
				if (itemMeta.hasLore())
					lore.addAll(itemMeta.getLore());
				lore.addAll(button.toButton(ShowButton.class).getLore(this.button));
				itemMeta.setLore(lore);
				itemStack.setItemMeta(itemMeta);
				addItem(button.getSlot(), itemStack);

			} else if (button.getType().equals(ButtonType.SET_TO_MAX)) {
				ItemStack itemStack = button.getItemStack();
				ItemMeta itemMeta = itemStack.getItemMeta();
				if (itemMeta.hasDisplayName())
					itemMeta.setDisplayName(
							itemMeta.getDisplayName().replace("%maxStack%", String.valueOf(this.button.getMaxStack())));
				itemStack.setItemMeta(itemMeta);
				addItem(button.getSlot(), itemStack);
			} else if (button.getType().equals(ButtonType.ADD) || button.getType().equals(ButtonType.REMOVE))
				addItem(button.getSlot(), button.getItemStack());
			else
				addItem(button.getSlot(), button.getItemStack()).setClick(clickEvent(main, player, page, button));
		});

		return InventoryResult.SUCCESS;
	}

	private Consumer<InventoryClickEvent> clickEvent(ZShop plugin, Player player, int page, Button currentButton) {
		return event -> {
			switch (currentButton.getType()) {
			case BUY_CONFIRM:
				button.buy(player, amount);
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
