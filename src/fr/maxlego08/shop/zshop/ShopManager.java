package fr.maxlego08.shop.zshop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.event.events.ShopOpenEvent;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.zshop.factories.Shop;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class ShopManager implements Shop {

	private final ZShop plugin;

	public ShopManager(ZShop plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void openShop(Player player, EnumCategory category, int page, Object... args) {

		if (Config.shopOpenEvent) {
			ShopOpenEvent event = new ShopOpenEvent(player, category);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
			category = event.getCategory();
		}
		
		plugin.getInventoryManager().createInventory(category.getInventoryID(), player, page, args);
	}

}
