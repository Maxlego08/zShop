package fr.maxlego08.shop.zshop.factories;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public interface Shop {

	/**
	 * Open the shop
	 * @param player
	 * @param category
	 * @param page
	 * @param args
	 */
	void openShop(Player player, EnumCategory category, int page, String permission, Object... args);
	
	/**
	 * @param player
	 * @param category
	 * @param page
	 * @param permission
	 * @param args
	 */
	void openShop(Player player, EnumCategory category, int page, Permission permission, Object... args);
	
	/**
	 * Open shop with category
	 * @param player
	 * @param category
	 */
	CommandType openShop(Player player, String category);
	
	/**
	 * Set item in hand
	 * @param player
	 * @param amount
	 */
	void sellHand(Player player, int amount);
	
	/**
	 * Sells all items inventory which are the same as the one being held in your hand
	 * @param player
	 */
	void sellAllHand(Player player);

	/**
	 * Sells all items
	 * @param player
	 */
	void sellAll(Player player);

	
}
