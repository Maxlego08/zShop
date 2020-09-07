package fr.maxlego08.shop.api;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.enums.PermissionType;
import fr.maxlego08.shop.api.history.HistoryManager;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.api.permission.Permission;

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
	public void open(Player player, Command command, ItemButton button, int page, List<Inventory> oldInventories,
			InventoryType type);
	
	/**
	 * Set item in hand
	 * 
	 * @param player
	 * @param amount
	 */
	void sellHand(Player player, int amount);

	/**
	 * Sells all items inventory which are the same as the one being held in
	 * your hand
	 * 
	 * @param player
	 */
	void sellAllHand(Player player);

	/**
	 * Sells all items
	 * 
	 * @param player
	 */
	void sellAll(Player player);
	
	/**
	 * Allows to retrieve an itembutton
	 * @param itemStack
	 * @return {@link Optional}
	 */
	public Optional<ItemButton> getItemButton(ItemStack itemStack);
	
	/**
	 * 
	 * @param permission
	 * @return
	 */
	public Optional<Permission> getPermission(String permission);
	
	/**
	 * Get permission
	 * @param player
	 * @param type
	 * @return {@link Permission}
	 */
	public Optional<Permission> getPermission(Player player, PermissionType type);

	/**
	 * Opens a specific category
	 * @param player
	 * @param command
	 * @param category
	 */
	public void open(Player player, Command command, String category);
	
	/**
	 * Get inventory by name
	 * @param name
	 * @return {@link Inventory}
	 */
	public Optional<Inventory> getInventoryByName(String name);

	/**
	 * 
	 * @return
	 */
	public HistoryManager getHistory();

}
