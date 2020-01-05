package fr.maxlego08.shop.zshop.categories;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.enums.Message;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zshop.factories.Categories;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class CategoryManager extends ZUtils implements Categories {

	private static Map<Integer, Category> categories = new HashMap<Integer, Category>();

	static {

		categories.put(1, (new Category(1, 0, ShopType.ITEM, "§aBlocks", new MaterialData(Material.GRASS), null, 54, 49,
				48, 50)));
		categories.put(2,
				(new Category(2, 1, ShopType.ITEM, "§eFarm", new MaterialData(Material.WHEAT), null, 54, 49, 48, 50)));
		categories.put(3, (new Category(3, 2, ShopType.ITEM, "§eMobs", new MaterialData(Material.ROTTEN_FLESH), null,
				54, 49, 48, 50)));
		categories.put(4, (new Category(4, 3, ShopType.ITEM, "§eFoods", new MaterialData(Material.COOKED_BEEF), null,
				54, 49, 48, 50)));
		categories.put(5, (new Category(5, 4, ShopType.ITEM, "§eRedstones", new MaterialData(Material.REDSTONE), null,
				54, 49, 48, 50)));
		categories.put(6, (new Category(6, 5, ShopType.ITEM, "§eOres", new MaterialData(Material.IRON_INGOT), null, 54,
				49, 48, 50)));
		categories.put(7, (new Category(7, 6, ShopType.UNIQUE_ITEM, "§eRanks",
				new MaterialData(Material.DIAMOND_CHESTPLATE), null, 54, 49, 48, 50)));
		categories.put(8, (new Category(8, 7, ShopType.ITEM, "§3Spawners",
				new MaterialData(getMaterial(52)), null, 54, 49, 48, 50)));

	}

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
	public void save(Persist persist) {
		persist.save(this, "categories");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, CategoryManager.class, "categories");
		testCategories();
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
			BaseComponent[] components = { 
					new TextComponent("§7§m-----------------------\n"),
					new TextComponent("§f§l» §7Id: §2" + c.getId() + "\n"),
					new TextComponent("§f§l» §7Name: §2" + c.getName() + "\n"),
					new TextComponent("§f§l» §7Type: §2" + c.getType().name().replace("_", " ") + "\n"),
					new TextComponent("§f§l» §7Inventory size: §2" + c.getInventorySize() + "\n"),
					new TextComponent("§7§m-----------------------")
					};
			HoverEvent event = new HoverEvent(Action.SHOW_TEXT, components);
			message.setHoverEvent(event);
			sender.spigot().sendMessage(message);

		});

	}

}
