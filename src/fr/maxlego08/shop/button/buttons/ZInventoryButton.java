package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.InventoryButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.ZPlugin;

public class ZInventoryButton extends ZPermissibleButton implements InventoryButton {

	protected String inventory;
	protected Inventory inventoryInterface;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param inventory
	 */
	public ZInventoryButton(ButtonType type, ItemStack itemStack, int slot, String inventory) {
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
			Button elseButton, String inventory) {
		super(type, itemStack, slot, permission, message, elseButton);
		this.inventory = inventory;
	}

	@Override
	public Inventory getInventory() {
		if (getType() == ButtonType.BACK)
			return inventoryInterface;
		return inventoryInterface == null
				? inventoryInterface = ((ZShop) ZPlugin.z()).getInventory().getInventory(inventory)
				: inventoryInterface;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZInventoryButton [inventory=" + inventory + ", inventoryInterface=" + inventoryInterface + "]";
	}

	
	
}
