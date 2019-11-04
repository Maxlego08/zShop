package fr.maxlego08.shop.zshop;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.zshop.factories.Shop;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class ShopManager implements Shop{

	private final ZShop plugin;
	
	public ShopManager(ZShop plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void openShop(Player player, EnumCategory category, int page, Object... args) {
		plugin.getInventoryManager().createInventory(category.getInventoryID(), player, page, args);
	}

	
}
