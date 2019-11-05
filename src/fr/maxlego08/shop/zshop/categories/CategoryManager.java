package fr.maxlego08.shop.zshop.categories;

import java.util.HashMap;
import java.util.Map;

import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zshop.factories.Categories;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;

public class CategoryManager implements Categories{

	private static Map<Integer, Category> categories = new HashMap<Integer, Category>();
	
	static{
		
		categories.put(1, (new Category(1, 10, ShopType.ITEM, "§aBlocks", 3, null, 9, 8, 0, 0)));
		categories.put(2, (new Category(2, 12, ShopType.ITEM, "§eFood", 364, null, 18, 13, 12, 14)));
		
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
