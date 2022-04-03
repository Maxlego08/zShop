package fr.maxlego08.shop.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.InventoryManager;
import fr.maxlego08.shop.api.Loader;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.exceptions.CategoriesNotFoundException;
import fr.maxlego08.shop.api.exceptions.InventoryFileNotFoundException;
import fr.maxlego08.shop.api.exceptions.InventoryNotFoundException;
import fr.maxlego08.shop.api.exceptions.InventorySizeException;
import fr.maxlego08.shop.api.exceptions.NameAlreadyExistException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.utils.loader.ItemStackLoader;
import fr.maxlego08.shop.zcore.utils.loader.button.ButtonCollections;
import fr.maxlego08.shop.zcore.utils.yaml.YamlUtils;

public class InventoryLoader extends YamlUtils implements InventoryManager {

	private final ZShop plugin;
	private final IEconomy economy;
	private final Map<InventoryType, Inventory> typeInventories = new HashMap<>();
	private final Map<String, Inventory> inventories = new HashMap<String, Inventory>();
	private List<String> defaultLore = new ArrayList<String>();

	/**
	 * @param plugin
	 * @param plugin2
	 * @param economy
	 */
	public InventoryLoader(ZShop plugin, IEconomy economy) {
		super(plugin);
		this.plugin = plugin;
		this.economy = economy;
	}

	@Override
	public Inventory getInventory(String name) {
		return this.getInventory(name, true);
	}

	/**
	 * 
	 * @param name
	 * @param throwError
	 * @return
	 */
	private Inventory getInventory(String name, boolean throwError) {
		if (name == null && throwError)
			throw new InventoryNotFoundException("Unable to find the inventory with name null");
		Inventory inventory = inventories.getOrDefault(name.toLowerCase(), null);
		if (inventory == null && throwError)
			throw new InventoryNotFoundException("Unable to find the inventory " + name);
		return inventory;
	}

	@Override
	public void loadInventories() throws Exception {

		info("Loading inventories in progress...");

		FileConfiguration config = super.getConfig();

		this.delete();

		defaultLore = color(config.getStringList("defaultItemLore"));

		if (!config.contains("categories"))
			throw new CategoriesNotFoundException("Cannot find the list of categories !");

		List<String> stringCategories = config.getStringList("categories");

		for (String stringCategory : stringCategories)
			this.loadInventory(stringCategory);

		info("Inventories loading complete.");

	}

	@Override
	public void saveInventories() {

	}

	@Override
	public Inventory loadInventory(String fileName) throws Exception {

		if (fileName == null)
			throw new NullPointerException("Impossible de trouver le string ! Il est null !");

		String lowerCategory = fileName.toLowerCase();

		if (inventories.containsKey(lowerCategory))
			throw new NameAlreadyExistException("the name " + lowerCategory
					+ " already exist ! (Simply remove it from the list of categories in the config.yml file.)");

		YamlConfiguration configuration = getConfig("inventories/" + lowerCategory + ".yml");

		if (configuration == null)
			throw new InventoryFileNotFoundException("Cannot find the file: inventories/" + lowerCategory + ".yml");

		InventoryType type = InventoryType.form(configuration.getString("type"));

		String name = configuration.getString("name");
		name = name == null ? "" : name;

		int size = configuration.getInt("size", 54);
		if (size % 9 != 0)
			throw new InventorySizeException("Size " + size + " is not valid for inventory " + lowerCategory);

		Loader<List<Button>> loader = new ButtonCollections(plugin, economy);
		List<Button> buttons = loader.load(configuration, lowerCategory);

		ItemStack itemStack = null;

		if (configuration.contains("fillItem")) {
			Loader<ItemStack> itemStackLoader = new ItemStackLoader();
			itemStack = itemStackLoader.load(configuration, "fillItem.");

		}

		Inventory inventory = new InventoryObject(name, type, size, buttons, lowerCategory, itemStack);
		inventories.put(lowerCategory, inventory);

		if (type != InventoryType.DEFAULT)
			typeInventories.put(type, inventory);

		success("Successful loading of the inventory " + lowerCategory + " !");
		return inventory;
	}

	@Override
	public void delete() {
		defaultLore.clear();
		inventories.clear();
		typeInventories.clear();
	}

	@Override
	public Inventory getInventory(InventoryType type) {
		return typeInventories.getOrDefault(type, null);
	}

	@Override
	public List<String> getLore() {
		return defaultLore;
	}

	@Override
	public Optional<ItemButton> getItemButton(ItemStack itemStack) {
		for (Inventory inventory : this.inventories.values()) {
			Optional<ItemButton> optional = inventory.getItemButton(itemStack);
			if (optional.isPresent())
				return optional;
		}
		return Optional.empty();
	}

	@Override
	public Optional<Inventory> getInventoryByName(String name) {
		Inventory inventory = getInventory(name, false);
		if (inventory != null)
			return Optional.of(inventory);
		return inventories.values().stream().filter(inv -> inv.getName().toLowerCase().contains(name.toLowerCase()))
				.findFirst();
	}

}
