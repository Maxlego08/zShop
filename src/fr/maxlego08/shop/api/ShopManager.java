package fr.maxlego08.shop.api;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.command.Command;

public interface ShopManager {

	/**
	 * 
	 * @throws Exception
	 */
	public void loadCommands() throws Exception;

	/**
	 * 
	 * @param player
	 * @param command
	 */
	public void open(Player player, Command command);

	/**
	 * 
	 */
	public void reload();
	
	/**
	 * Close players inventory
	 */
	public void closeInventory();
	
}
