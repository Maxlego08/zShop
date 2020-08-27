package fr.maxlego08.shop.api.button.buttons;

import fr.maxlego08.shop.api.inventory.Inventory;

public interface InventoryButton extends PermissibleButton{

	public Inventory getInventory();
	
}
