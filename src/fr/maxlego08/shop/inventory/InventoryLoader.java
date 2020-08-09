package fr.maxlego08.shop.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxlego08.shop.api.InventoryManager;
import fr.maxlego08.shop.api.Loader;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.exceptions.CategoriesNotFoundException;
import fr.maxlego08.shop.api.exceptions.InventoryFileNotFoundException;
import fr.maxlego08.shop.api.exceptions.InventorySizeException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.utils.loader.button.ButtonCollections;
import fr.maxlego08.shop.zcore.utils.yaml.YamlUtils;

public class InventoryLoader extends YamlUtils implements InventoryManager {

	public InventoryLoader(JavaPlugin plugin) {
		super(plugin);
	}

	private final Map<String, Inventory> inventories = new HashMap<String, Inventory>();

	@Override
	public Inventory getInventory(String name) {
		return inventories.getOrDefault(name.toLowerCase(), null);
	}

	@Override
	public void loadInventories() throws Exception {

		info("Loading inventories in progress...");

		FileConfiguration config = super.getConfig();

		if (!config.contains("categories"))
			throw new CategoriesNotFoundException("Cannot find the list of categories !");

		List<String> stringCategories = config.getStringList("categories");

		for (String stringCategory : stringCategories) {

			String lowerCategory = stringCategory.toLowerCase();
			YamlConfiguration configuration = getConfig("inventories/" + lowerCategory + ".yml");

			if (configuration == null)
				throw new InventoryFileNotFoundException("Cannot find the file: inventories/" + lowerCategory + ".yml");

			String name = configuration.getString("name");
			name = name == null ? "" : name;

			int size = configuration.getInt("size");
			if (size % 9 != 0)
				throw new InventorySizeException("Size " + size + " is not valid for inventory " + lowerCategory);

			Loader<List<Button>> loader = new ButtonCollections();
			List<Button> buttons = loader.load(configuration, lowerCategory);

			Inventory inventory = new InventoryObject(name, size, buttons);
			inventories.put(lowerCategory, inventory);
			success("Successful loading of the inventory " + lowerCategory + " !");

		}

		info("Inventories loading complete.");

	}

	@Override
	public void saveInventories() {

	}

}
