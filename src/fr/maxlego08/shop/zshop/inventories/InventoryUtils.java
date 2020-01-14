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
			Map<Integer, Button> decorations = new HashMap<>();
			path = path + "buttons";
			if (configuration.get(path) != null)
				for (String u : configuration.getConfigurationSection(path + ".").getKeys(false)) {
					String tmpPath = path + "." + u + ".";
					int slot = configuration.getInt(tmpPath + "slot");
					Button button = buttons.load(configuration, tmpPath + "button.");
					decorations.put(slot, button);
				}
			inventories.put(id, new InventoryObject(this.categories.getCategories(categories), id, inventorySize, name,
					decorations));

		}

		Logger.info(file.getAbsolutePath() + " loaded successfully !", LogType.SUCCESS);
		Logger.info("Loading " + inventories.size() + " inventories", LogType.SUCCESS);

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
			v.getDecorations().forEach((slot, button) -> {
				configuration.set(path + "buttons." + integer.get() + ".slot", slot);
				buttonLoader.save(button, configuration, path + "buttons." + integer.get() + ".button.");
				integer.getAndIncrement();
			});
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

		InventoryObject obj = new InventoryObject(categories.getCategories(1, 2, 3, 4, 5, 6), 1, 9, "§7Shop",
				new HashMap<>());
		this.inventories.put(1, obj);

		int[] array = new int[] { 13, 13, 5, 5, 5, 5, 5, 13, 13, 13, 5, 5, 4, 4, 4, 5, 5, 13, 5, 5, 4, 4, 4, 4, 4, 5, 5,
				13, 5, 5, 4, 4, 4, 5, 5, 13, 13, 13, 5, 5, 5, 5, 5, 13, 13 };

		Map<Integer, Button> buttons = new HashMap<>();
		for(int slot = 0; slot != array.length; slot++)
			buttons.put(slot, new Button("", 160, array[slot]));
		
		obj = new InventoryObject(categories.getCategories(8, 7), 1, 45, "§cVip", buttons);
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
