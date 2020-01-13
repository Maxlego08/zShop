package fr.maxlego08.shop.zshop.factories;

import java.util.Map;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.zshop.categories.Category;

public interface Categories{

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
	
	/**
	 * @param name
	 * @return
	 */
	Category getCategory(String name);

	/**
	 * @param commandSender
	 */
	void showInformation(Player sender);


	void load();
	
	void save();
	
	void saveDefault();
	
}
