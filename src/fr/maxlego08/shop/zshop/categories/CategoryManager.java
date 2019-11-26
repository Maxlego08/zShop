package fr.maxlego08.shop.zshop.categories;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Material;

import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zshop.factories.Categories;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;

public class CategoryManager implements Categories {

	private static Map<Integer, Category> categories = new HashMap<Integer, Category>();

	static {

		categories.put(1, (new Category(1, 0, ShopType.ITEM, "§aBlocks", new MaterialData(Material.GRASS), null, 54, 49,
				48, 50, "zshop.category.bloks")));
		categories.put(2, (new Category(2, 1, ShopType.ITEM, "§eFarm", new MaterialData(Material.WHEAT), null, 54, 49,
				48, 50, "zshop.category.farm")));
		categories.put(3, (new Category(3, 2, ShopType.ITEM, "§eMobs", new MaterialData(Material.ROTTEN_FLESH), null,
				54, 49, 48, 50, "zshop.category.mobs")));
		categories.put(4, (new Category(4, 3, ShopType.ITEM, "§eFoods", new MaterialData(Material.COOKED_BEEF), null,
				54, 49, 48, 50, "zshop.category.foods")));
		categories.put(5, (new Category(5, 4, ShopType.ITEM, "§eRedstones", new MaterialData(Material.REDSTONE), null,
				54, 49, 48, 50, "zshop.category.redstone")));
		categories.put(6, (new Category(6, 5, ShopType.ITEM, "§eOres", new MaterialData(Material.IRON_INGOT), null, 54,
				49, 48, 50, "zshop.category.ores")));
		categories.put(7, (new Category(7, 5, ShopType.UNIQUE_ITEM, "§eRanks",
				new MaterialData(Material.DIAMOND_CHESTPLATE), null, 54, 49, 48, 50, "zshop.category.ranks")));

	}

	private void testCategories() {

		Iterator<Category> iterator = categories.values().iterator();
		while (iterator.hasNext()) {
			Category category = iterator.next();

			if (!category.isCorrect()) {
				Logger.info(
						"Could not load category correctly with id " + category.getId()
								+ ". Please check if all items are well contained in the inventory size",
						LogType.ERROR);
			}

		}
	}

	@Override
	public void save(Persist persist) {
		persist.save(this, "categories");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, CategoryManager.class, "categories");
		testCategories();
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
