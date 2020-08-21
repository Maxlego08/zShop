package fr.maxlego08.shop.api;

import java.util.List;

import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.inventory.Inventory;

public interface InventoryManager {

	/**
	 * Get inventory by name
	 * @param name
	 * @return inventory
	 */
	public Inventory getInventory(String name);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public Inventory getInventory(InventoryType type);
	
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

	/**
	 * Delete all inventory
	 */
	public void delete();

	public List<String> getLore();
	
}
