package fr.maxlego08.shop.zshop.categories;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zshop.factories.Categories;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;

public class CategoryManager implements Categories{

	private static Map<Integer, Category> categories = new HashMap<Integer, Category>();
	
	static{
		
		categories.put(1, (new Category(1, 0, ShopType.ITEM, "§aBlocks", new MaterialData(Material.GRASS), null, 54, 49, 49, 50)));
		categories.put(2, (new Category(2, 1, ShopType.ITEM, "§eFarm",new MaterialData(Material.WHEAT), null, 54, 49, 49, 50)));
		categories.put(3, (new Category(3, 2, ShopType.ITEM, "§eMobs",new MaterialData(Material.ROTTEN_FLESH), null, 54, 49, 49, 50)));
		categories.put(4, (new Category(4, 3, ShopType.ITEM, "§eFoods",new MaterialData(Material.COOKED_BEEF), null, 54, 49, 49, 50)));
		categories.put(5, (new Category(5, 4, ShopType.ITEM, "§eRedstones",new MaterialData(Material.REDSTONE), null, 54, 49, 49, 50)));
		categories.put(6, (new Category(6, 5, ShopType.ITEM, "§eOres",new MaterialData(Material.IRON_INGOT), null, 54, 49, 49, 50)));
		
	}
	
	@Override
	public void save(Persist persist) {
		persist.save(this, "categories");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, CategoryManager.class, "categories");
	}

	@Override
	public Map<Integer, Category> getCategories() {
		return categories;
	}

	@Override
	public Category getCategory(int id) {
		return categories.getOrDefault(id, null);
	}

}
