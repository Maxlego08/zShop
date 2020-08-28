package fr.maxlego08.shop.api.command;

import java.util.List;

import fr.maxlego08.shop.api.inventory.Inventory;

public interface Command {

	/**
	 * 
	 * @return
	 */
	public String getCommand();
	
	/**
	 * 
	 * @return
	 */
	public String getPermission();
	
	/**
	 * 
	 * @return
	 */
	public String getDescription();
	
	/**
	 * 
	 * @return
	 */
	public List<String> getAliases();
	
	/**
	 * 
	 * @return
	 */
	public Inventory getInventory();
	
	/**
	 * Get optional command
	 * @param action
	 * @return {@link OptionalAction}
	 */
	public OptionalCommand getOptionalCommand(OptionalAction action);
	
}
