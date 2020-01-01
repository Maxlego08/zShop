package fr.maxlego08.shop;

import org.bukkit.plugin.ServicePriority;

import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.inventory.InventoryManager;
import fr.maxlego08.shop.listener.AdapterListener;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.utils.Metrics;
import fr.maxlego08.shop.zcore.utils.VersionChecker;
import fr.maxlego08.shop.zshop.ShopManager;
import fr.maxlego08.shop.zshop.categories.CategoryManager;
import fr.maxlego08.shop.zshop.factories.Categories;
import fr.maxlego08.shop.zshop.factories.Items;
import fr.maxlego08.shop.zshop.factories.Shop;
import fr.maxlego08.shop.zshop.items.ShopItemManager;

public class ZShop extends ZPlugin {

	private CommandManager commandManager;
	private InventoryManager inventoryManager;
	private Shop shop;
	private Categories categories;
	private Items items;
	private static ZShop instance;

	@Override
	public void onEnable() {

		instance = this;

		preEnable();

		commandManager = new CommandManager(this);
		commandManager.registerCommands();

		if (!isEnabled())
			return;

		inventoryManager = new InventoryManager(this);
		shop = new ShopManager(this);
		categories = new CategoryManager();
		items = new ShopItemManager(this);

		getServer().getServicesManager().register(Shop.class, shop, this, ServicePriority.High);
		getServer().getServicesManager().register(Categories.class, categories, this, ServicePriority.High);
		getServer().getServicesManager().register(Items.class, items, this, ServicePriority.High);

		VersionChecker versionChecker = new VersionChecker();
		versionChecker.getLastVersion();

		/* Add Listener */

		addListener(new AdapterListener(this));
		addListener(inventoryManager);
		addListener(versionChecker);

		/* Add Saver */

		addSave(new Config());
		addSave(new Lang());
		addSave(categories);

		getSavers().forEach(saver -> saver.load(getPersist()));
		items.load();

		new Metrics(this);

		postEnable();

	}

	@Override
	public void onDisable() {

		preDisable();

		inventoryManager.closeAllPlayer();

		getSavers().forEach(saver -> saver.save(getPersist()));

		postDisable();

	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public Categories getCategories() {
		return categories;
	}

	public Shop getShop() {
		return shop;
	}

	public Items getItems() {
		return items;
	}

	public static ZShop i() {
		return instance;
	}

}
