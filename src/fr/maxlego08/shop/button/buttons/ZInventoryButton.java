package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.InventoryButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.inventory.Inventory;

public class ZInventoryButton extends ZPermissibleButton implements InventoryButton {

	protected String inventory;
	protected Inventory inventoryInterface;
	private final ZShop plugin;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param inventory
	 */
	public ZInventoryButton(ZShop plugin, ButtonType type, ItemStack itemStack, int slot, String inventory, boolean isPermanent) {
		super(type, itemStack, slot, isPermanent);
		this.inventory = inventory;
		this.plugin = plugin;
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
	public ZInventoryButton(ZShop plugin,ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, String inventory, boolean isPermanent) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent);
		this.inventory = inventory;
		this.plugin = plugin;
	}

	@Override
	public Inventory getInventory() {
		if (getType() == ButtonType.BACK)
			return inventoryInterface;
		return inventoryInterface == null
				? inventoryInterface = plugin.getInventory().getInventory(inventory)
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
