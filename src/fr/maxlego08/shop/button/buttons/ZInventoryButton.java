package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.InventoryButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.inventory.Inventory;

public class ZInventoryButton extends ZPermissibleButton implements InventoryButton {

	protected Inventory inventory;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param inventory
	 */
	public ZInventoryButton(ButtonType type, ItemStack itemStack, int slot, Inventory inventory) {
		super(type, itemStack, slot);
		this.inventory = inventory;
	}

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param inventory
	 */
	public ZInventoryButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, Inventory inventory) {
		super(type, itemStack, slot, permission, message, elseButton);
		this.inventory = inventory;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

}
