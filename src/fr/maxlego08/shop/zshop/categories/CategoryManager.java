package fr.maxlego08.shop.zshop.categories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.builder.ItemBuilder;
import fr.maxlego08.shop.zcore.utils.builder.OLDItemBuilder;
import fr.maxlego08.shop.zcore.utils.enums.Message;
import fr.maxlego08.shop.zshop.factories.Categories;
import fr.maxlego08.shop.zshop.factories.ShopItem.ShopType;
import fr.maxlego08.shop.zshop.utils.ItemStackYAMLoader;
import fr.maxlego08.shop.zshop.utils.Loader;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class CategoryManager extends ZUtils implements Categories {

	private Map<Integer, Category> categories = new HashMap<Integer, Category>();

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
	public Map<Integer, Category> getCategories() {
		return categories;
	}

	@Override
	public Category getCategory(int id) {
		return categories.getOrDefault(id, null);
	}

	@Override
	public Category getCategory(String name) {
		return categories.values().stream()
				.filter(category -> category.getName().toLowerCase().contains(name.toLowerCase())).findAny()
				.orElse(null);
	}

	@Override
	public void showInformation(Player sender) {

		message(sender, Message.CATEGORY_INFORMATION);
		categories.values().forEach(c -> {

			TextComponent message = new TextComponent(
					Lang.prefix + " §eCategory§8: §6" + c.getName() + " §7(hover me)");
			BaseComponent[] components = { new TextComponent("§7§m-----------------------\n"),
					new TextComponent("§f§l» §7Id: §2" + c.getId() + "\n"),
					new TextComponent("§f§l» §7Name: §2" + c.getName() + "\n"),
					new TextComponent("§f§l» §7Type: §2" + c.getType().name().replace("_", " ") + "\n"),
					new TextComponent("§f§l» §7Inventory size: §2" + c.getInventorySize() + "\n"),
					new TextComponent("§7§m-----------------------") };
			HoverEvent event = new HoverEvent(Action.SHOW_TEXT, components);
			message.setHoverEvent(event);
			sender.spigot().sendMessage(message);

		});

	}

	@Override
	public void load() {

		File file = new File(plugin.getDataFolder() + File.separator + "categories.yml");
		if (!file.exists())
			this.saveDefault();

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

		if (configuration.getString("categories") == null) {
			Logger.info("Impossible de charger les categories !", LogType.ERROR);
			return;
		}

		Loader<ItemStack> itemStackYAMLoader = new ItemStackYAMLoader();

		// Chargement des categories
		for (String categoryId : configuration.getConfigurationSection("categories.").getKeys(false)) {

			String path = "categories." + categoryId + ".";

			String name = color(configuration.getString(path + "name", "Default name"));
			ItemStack itemStack = itemStackYAMLoader.load(configuration, path + "items.");
			ShopType type = ShopType.valueOf(configuration.getString(path + "type"));
			int slot = configuration.getInt(path + "slot", 0);
			int inventoryId = configuration.getInt(path + "inventoryId", 0);
			int inventorySize = configuration.getInt(path + "inventorySize", 0);
			int backButtonSlot = configuration.getInt(path + "backButtonSlot", 0);
			int previousButtonSlot = configuration.getInt(path + "previousButtonSlot", 0);
			int nexButtonSlot = configuration.getInt(path + "nexButtonSlot", 0);

			int id = Integer.valueOf(categoryId);

			Category category = new Category(id, slot, inventoryId, type, name, itemStack, inventorySize,
					backButtonSlot, previousButtonSlot, nexButtonSlot);
			this.categories.put(id, category);

		}

		Logger.info(file.getAbsolutePath() + " loaded successfully !", LogType.SUCCESS);
		Logger.info("Loading " + categories.size() + " categories", LogType.SUCCESS);

		testCategories();
	}

	@Override
	public void save() {
		if (categories == null)
			throw new IllegalArgumentException("La liste des categories est null !");

		File file = new File(plugin.getDataFolder() + File.separator + "categories.yml");
		if (file.exists())
			file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		ItemStackYAMLoader stackYAMLoader = new ItemStackYAMLoader();

		this.categories.forEach((id, c) -> {
			String path = "categories." + id + ".";
			configuration.set(path + "name", colorReverse(c.getName()));
			configuration.set(path + "type", c.getType().name());
			stackYAMLoader.save(c.getData(), configuration, path + "items.");
			configuration.set(path + "slot", c.getSlot());
			configuration.set(path + "inventoryId", c.getInventoryId());
			configuration.set(path + "inventorySize", c.getInventorySize());
			configuration.set(path + "backButtonSlot", c.getBackButtonSlot());
			configuration.set(path + "previousButtonSlot", c.getPreviousButtonSlot());
			configuration.set(path + "nexButtonSlot", c.getNexButtonSlot());
		});
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveDefault() {

		categories.clear();

		categories.put(1, (new Category(1, 0, 1, ShopType.ITEM_SLOT, "§aBlocks",
				new ItemBuilder(Material.GRASS, "§aBlocks").build(), 54, 49, 48, 50)));

		categories.put(2, (new Category(2, 1, 1, ShopType.ITEM_SLOT, "§eFarm",
				new ItemBuilder(Material.WHEAT, "§eFarm").build(), 36, 31, 30, 32)));

		categories.put(3, (new Category(3, 2, 1, ShopType.ITEM, "§eMobs",
				new ItemBuilder(Material.ROTTEN_FLESH, "§eMobs").build(), 54, 49, 48, 50)));

		categories.put(4, (new Category(4, 3, 1, ShopType.ITEM, "§eFoods",
				new ItemBuilder(Material.COOKED_BEEF, "§eFoods").build(), 54, 49, 48, 50)));

		categories.put(5, (new Category(5, 4, 1, ShopType.ITEM, "§eRedstones",
				new ItemBuilder(Material.REDSTONE, "§eRedstones").build(), 54, 49, 48, 50)));

		categories.put(6, (new Category(6, 5, 1, ShopType.ITEM, "§eOres",
				new ItemBuilder(Material.IRON_INGOT, "§eOres").build(), 54, 49, 48, 50)));

		categories.put(7, (new Category(7, 23, 1, ShopType.UNIQUE_ITEM, "§eRanks",
				OLDItemBuilder.getCreatedItem(Material.DIAMOND_CHESTPLATE, 1, "§cRanks"), 54, 49, 48, 50)));
		categories.put(8, (new Category(8, 21, 1, ShopType.ITEM, "§3Spawners",
				OLDItemBuilder.getCreatedItem(getMaterial(52), 1, "§a§nSpawners"), 54, 49, 48, 50)));

		this.save();
	}

	@Override
	public List<Category> getCategories(List<Integer> id) {
		List<Category> categories = new ArrayList<Category>();
		for (int i : id) {
			Category c = getCategory(i);
			if (c != null)
				categories.add(c);
		}
		return categories;
	}

	@Override
	public List<Category> getCategories(int... id) {
		List<Integer> integers = new ArrayList<>();
		for (int i : id)
			integers.add(i);
		return getCategories(integers);
	}

	@Override
	public List<String> toTabCompleter() {

		List<Category> categories = new ArrayList<>(this.categories.values());
		return categories.stream().map(category -> removeColor(category.getName())).collect(Collectors.toList());
		
	}

}
