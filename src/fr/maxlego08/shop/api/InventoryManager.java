package fr.maxlego08.shop.api;

import fr.maxlego08.shop.api.inventory.Inventory;

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
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public Inventory loadInventory(String fileName) throws Exception;
	
	/**
	 * Save inventories
	 */
	public void saveInventories();
	
}
