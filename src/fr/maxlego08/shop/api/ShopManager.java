package fr.maxlego08.shop.api;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.inventory.Inventory;

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
	
	/**
	 * 
	 * @return
	 */
	public IEconomy getIEconomy();

	/**
	 * 
	 * @param player
	 * @param command
	 * @param button
	 * @param page
	 * @param inventory
	 */
	public void open(Player player, Command command, ItemButton button, int page, Inventory inventory, InventoryType type);
	
}
