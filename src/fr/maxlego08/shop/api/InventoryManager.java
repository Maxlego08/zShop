package fr.maxlego08.shop.api;

import org.bukkit.inventory.Inventory;

public interface InventoryManager {

	/**
	 * Get inventory by name
	 * @param name
	 * @return inventory
	 */
	public Inventory getInventory(String name);
	
	/**
	 * Load inventories
	 */
	public void loadInventories() throws Exception;
	
	/**
	 * Save inventories
	 */
	public void saveInventories();
	
}
