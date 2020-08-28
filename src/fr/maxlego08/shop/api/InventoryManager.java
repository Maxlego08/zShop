package fr.maxlego08.shop.api;

import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.buttons.ItemButton;
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

	/**
	 * 
	 * @return
	 */
	public List<String> getLore();
	
	/**
	 * Allows to retrieve an itembutton
	 * @param itemStack
	 * @return {@link Optional}
	 */
	public Optional<ItemButton> getItemButton(ItemStack itemStack);
	
}
