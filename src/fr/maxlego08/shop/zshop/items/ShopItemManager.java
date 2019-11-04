package fr.maxlego08.shop.zshop.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zshop.factories.Items;

public class ShopItemManager implements Items {

	private transient final ZShop plugin;

	private static Map<Integer, List<ShopItem>> items = new HashMap<Integer, List<ShopItem>>();
	
	@Override
	public void save(Persist persist) {
		persist.save(this, "items");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, ShopItemManager.class, "items");
	}

	public ShopItemManager(ZShop plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public List<ShopItem> getItems(int category) {
		return items.getOrDefault(category, new ArrayList<>());
	}
	
	
	
}
