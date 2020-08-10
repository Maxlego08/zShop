package fr.maxlego08.shop.api.button.buttons;

import fr.maxlego08.shop.api.inventory.Inventory;

public interface BackButton extends InventoryButton {

	/**
	 * Set back inventory
	 * @param inventory
	 */
	public void setBackInventory(Inventory inventory);
	
}
