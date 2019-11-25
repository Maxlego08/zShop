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
	
}
