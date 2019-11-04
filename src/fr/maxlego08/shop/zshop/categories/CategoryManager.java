package fr.maxlego08.shop.zshop.categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zshop.factories.Categories;

public class CategoryManager implements Categories{

	private static List<Category> categories = new ArrayList<Category>();
	
	static{
		
		categories.add(new Category(1, 10, "§aBlocks", 3, Arrays.asList("")));
		categories.add(new Category(1, 12, "§eFood", 364, Arrays.asList("")));
		
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
	public List<Category> getCategories() {
		return categories;
	}

}
