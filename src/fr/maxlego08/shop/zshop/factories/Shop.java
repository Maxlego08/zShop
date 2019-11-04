package fr.maxlego08.shop.zshop.factories;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.zshop.utils.EnumCategory;

public interface Shop {

	void openShop(Player player, EnumCategory category, int page, Object... args);
	
}
