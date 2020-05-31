package fr.maxlego08.shop.zshop.inventories;

import org.bukkit.command.Command;

public interface Inventories {

	/**
	 * 
	 */
	void load();

	/**
	 * 
	 */
	void save();

	/**
	 * 
	 */
	void saveDefault();

	/**
	 * 
	 * @param id
	 * @return
	 */
	InventoryObject getInventory(int id);

	/**
	 * 
	 * @param command
	 * @return
	 */
	InventoryObject getInventory(Command command);

}
