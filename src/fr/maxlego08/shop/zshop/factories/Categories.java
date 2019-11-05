package fr.maxlego08.shop.zshop.factories;

import java.util.Map;

import fr.maxlego08.shop.zcore.utils.storage.Saveable;
import fr.maxlego08.shop.zshop.categories.Category;

public interface Categories extends Saveable{

	Map<Integer, Category> getCategories();
	
	Category getCategory(int id);
	
}
