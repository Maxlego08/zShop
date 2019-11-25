package fr.maxlego08.shop.zshop.factories;

import java.util.Map;

import fr.maxlego08.shop.zcore.utils.storage.Saveable;
import fr.maxlego08.shop.zshop.categories.Category;

public interface Categories extends Saveable {

	/**
	 * Allows you to retrieve all available categories
	 * 
	 * @return an array of int and category
	 */
	Map<Integer, Category> getCategories();

	/**
	 * Allows you to retrieve a category based on an id
	 * 
	 * @param category
	 *            id
	 * @return category according to the id
	 */
	Category getCategory(int id);

}
