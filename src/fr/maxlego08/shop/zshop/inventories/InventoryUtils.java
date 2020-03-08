package fr.maxlego08.shop.zshop.inventories;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.inventory.Button;
import fr.maxlego08.shop.zshop.factories.Categories;
import fr.maxlego08.shop.zshop.utils.ButtonLoader;
import fr.maxlego08.shop.zshop.utils.Loader;

public class InventoryUtils extends ZUtils implements Inventories {

	private Map<Integer, InventoryObject> inventories = new HashMap<>();
	private final Categories categories;

	public InventoryUtils(Categories categories) {
		super();
		this.categories = categories;
	}

	@Override
	public void load() {

		File file = new File(plugin.getDataFolder() + File.separator + "inventories.yml");
		if (!file.exists())
			this.saveDefault();

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		Loader<Button> buttons = new ButtonLoader();

		inventories.clear();

		if (configuration.getString("inventories") == null) {
			Logger.info("Impossible de charger les inventaires !", LogType.ERROR);
			return;
		}

		for (String categoryId : configuration.getConfigurationSection("inventories.").getKeys(false)) {

			String path = "inventories." + categoryId + ".";
			int id = Integer.valueOf(categoryId);
			String name = color(configuration.getString(path + "name"));
			int inventorySize = configuration.getInt(path + "inventorySize", 54);
			List<Integer> categories = configuration.getIntegerList(path + "categories");
			Map<Integer, Map<Integer, Button>> decorations = new HashMap<>();
			path = path + "buttons";
			if (configuration.get(path) != null)
				for (String u : configuration.getConfigurationSection(path + ".").getKeys(false)) {
					String tmpPath = path + "." + u + ".";
					int slot = configuration.getInt(tmpPath + "slot");
					int category = configuration.getInt(tmpPath + "category", 0);
					Button button = buttons.load(configuration, tmpPath + "button.");
					button.setCategory(category);
					this.fillMap(decorations, category, button, slot);
				}
			
			this.inventories.put(id, new InventoryObject(this.categories.getCategories(categories), id, inventorySize,
					name, decorations));

		}

		Logger.info(file.getAbsolutePath() + " loaded successfully !", LogType.SUCCESS);
		Logger.info("Loading " + inventories.size() + " inventories", LogType.SUCCESS);

	}

	/**
	 * Permet de remplir la map
	 * 
	 * @param decorations
	 * @param category
	 * @param button
	 * @param slot
	 */
	private void fillMap(Map<Integer, Map<Integer, Button>> decorations, int category, Button button, int slot) {
		// On ajoute la map pour l'id s'il n'existe pas
		if (!decorations.containsKey(category)) {
			decorations.put(category, new HashMap<>());
		}
		
		decorations.get(category).put(slot, button);
	}

	@Override
	public void save() {

		File file = new File(plugin.getDataFolder() + File.separator + "inventories.yml");
		if (file.exists())
			file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		Loader<Button> buttonLoader = new ButtonLoader();

		this.inventories.forEach((id, v) -> {
			String path = "inventories." + id + ".";
			configuration.set(path + "name", colorReverse(v.getName()));
			configuration.set(path + "inventorySize", v.getInventorySize());
			List<Integer> categories = v.getCategories().stream().map(c -> c.getId()).collect(Collectors.toList());
			configuration.set(path + "categories", categories);
			AtomicInteger integer = new AtomicInteger(1);
			v.getAll().forEach((b, m) -> m.forEach((slot, button) -> {
				configuration.set(path + "buttons." + integer.get() + ".slot", slot);
				configuration.set(path + "buttons." + integer.get() + ".category", b);
				buttonLoader.save(button, configuration, path + "buttons." + integer.get() + ".button.");
				integer.getAndIncrement();
			}));
		});
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveDefault() {

		this.inventories.clear();

		Map<Integer, Map<Integer, Button>> b = new HashMap<>();
		Map<Integer, Button> buttons = new HashMap<>();

		int[] array = new int[] { 5, 5, 8, 8, 8, 8, 8, 5, 5, 5, 8, 8, 5, 8, 8, 8, 8, 5, 8, 8, 5, 5, 5, 8, 8, 8, 8, 8, 5,
				5 };
		int[] slots = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47,
				48, 49, 50, 51, 52, 53 };

		for (int a = 0; a != slots.length; a++) {
			Button button = new Button("", 160, array[a], 1);
			button.setCategory(1);
			buttons.put(slots[a], button);
		}
		b.put(1, buttons);

		Map<Integer, Button> buttons2 = new HashMap<>();

		int[] arrayss = new int[] { 7, 14, 7, 7, 7, 7, 7, 14, 7, 14, 14, 14, 14, 7, 14, 7, 7, 7, 7, 7, 14, 7 };
		int[] slotsss = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 };

		for (int a1 = 0; a1 != slotsss.length; a1++) {
			Button button = new Button("", 160, arrayss[a1], 2);
			button.setCategory(2);
			buttons2.put(slotsss[a1], button);
		}
		b.put(2, buttons2);

		InventoryObject obj = new InventoryObject(categories.getCategories(1, 2, 3, 4, 5, 6), 1, 9, "§7Shop", b);
		this.inventories.put(1, obj);

		int[] arrays = new int[] { 13, 13, 5, 5, 5, 5, 5, 13, 13, 13, 5, 5, 4, 4, 4, 5, 5, 13, 5, 5, 4, 4, 4, 4, 4, 5,
				5, 13, 5, 5, 4, 4, 4, 5, 5, 13, 13, 13, 5, 5, 5, 5, 5, 13, 13 };

		Map<Integer, Map<Integer, Button>> b1 = new HashMap<>();
		Map<Integer, Button> buttons1 = new HashMap<>();

		for (int slot = 0; slot != array.length; slot++)
			buttons1.put(slot, new Button("", 160, arrays[slot]));
		b1.put(0, buttons1);

		obj = new InventoryObject(categories.getCategories(8, 7), 1, 45, "§cVip", b1);
		this.inventories.put(2, obj);

		this.save();

	}

	@Override
	public InventoryObject getInventory(int id) {
		return inventories.getOrDefault(id, null);
	}

	@Override
	public InventoryObject getInventory(Command command) {
		return inventories.values().stream().filter(i -> i.match(command)).findAny().orElse(null);
	}

}
