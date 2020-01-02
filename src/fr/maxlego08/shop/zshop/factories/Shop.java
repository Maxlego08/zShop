package fr.maxlego08.shop.zshop.factories;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.zshop.utils.EnumCategory;

public interface Shop {

	/**
	 * Open the shop
	 * @param player
	 * @param category
	 * @param page
	 * @param args
	 */
	void openShop(Player player, EnumCategory category, int page, Object... args);
	
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
