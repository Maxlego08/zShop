package fr.maxlego08.shop.zshop.inventories;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;

import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.zcore.utils.inventory.Button;
import fr.maxlego08.shop.zshop.categories.Category;

public class InventoryObject {

	private final List<Category> categories;
	private final int id;
	private final String name;
	private final int inventorySize;
	private final Map<Integer, Button> decorations;

	public InventoryObject(List<Category> categories, int id, int inventorySize, String name, Map<Integer, Button> decorations) {
		super();
		this.categories = categories;
		this.id = id;
		this.name = name;
		this.decorations = decorations;
		this.inventorySize = inventorySize;
	}

	public int getInventorySize() {
		return inventorySize;
	}
	
	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the decorations
	 */
	public Map<Integer, Button> getDecorations() {
		return decorations;
	}

	public boolean match(Command command) {
		return Config.commands.stream().filter(cmd -> cmd.match(command, id)).findAny().isPresent();
	}

}
