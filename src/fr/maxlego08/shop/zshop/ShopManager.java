package fr.maxlego08.shop.zshop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.event.events.ShopOpenEvent;
import fr.maxlego08.shop.event.events.ShopPostSellAllEvent;
import fr.maxlego08.shop.event.events.ShopPreSellAllEvent;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.categories.Category;
import fr.maxlego08.shop.zshop.factories.Items;
import fr.maxlego08.shop.zshop.factories.Shop;
import fr.maxlego08.shop.zshop.factories.ShopItem;
import fr.maxlego08.shop.zshop.factories.ShopItem.ShopType;
import fr.maxlego08.shop.zshop.inventories.Inventories;
import fr.maxlego08.shop.zshop.inventories.InventoryObject;
import fr.maxlego08.shop.zshop.items.Economy;
import fr.maxlego08.shop.zshop.utils.EnumCategory;
import net.citizensnpcs.api.npc.NPC;

public class ShopManager extends ZUtils implements Shop {

	private final ZShop plugin;

	private final Items items;
	private final Inventories inventories;

	public ShopManager(ZShop plugin) {
		super();
		this.plugin = plugin;
		this.items = plugin.getItems();
		this.inventories = plugin.getInventory();
	}

	@Override
	public void openShop(Player player, EnumCategory category, int page, int id, String permission, Object... args) {

		plugin.getBoost().updateBoost();

		if (Config.shopOpenEvent) {
			ShopOpenEvent event = new ShopOpenEvent(player, category);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
			category = event.getCategory();
		}

		if (permission != null && !player.hasPermission(permission)) {
			message(player, Lang.noPermission);
			return;
		}

		plugin.getInventoryManager().createInventory(category.getInventoryID(), player, page,
				inventories.getInventory(id), args);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void sellHand(Player player, int amount) {

		if (player.getItemInHand().getType().equals(Material.AIR)) {
			message(player, Lang.sellHandAir);
			return;
		}

		List<ShopItem> sellItems = items.getItems(player.getItemInHand());

		if (sellItems.size() == 0) {
			message(player, Lang.sellHandEmpty);
			return;
		}

		ShopItem item = sellItems.get(0);
		item.performSell(player, amount);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void sellAllHand(Player player) {

		if (player.getItemInHand().getType().equals(Material.AIR)) {
			message(player, Lang.sellHandAir);
			return;
		}

		List<ShopItem> sellItems = items.getItems(player.getItemInHand());

		if (sellItems.size() == 0) {
			message(player, Lang.sellHandEmpty);
			return;
		}

		ShopItem item = sellItems.get(0);
		item.performSell(player, 0);

	}

	@Override
	public void sellAll(Player player) {

		double price = 0;
		Map<ItemStack, Integer> map = new HashMap<ItemStack, Integer>();
		Economy eco = null;

		// On parcours l'inventaire du joueur
		for (ItemStack is : player.getInventory().getContents()) {
			// On verif si l'item est pas null
			if (is != null) {
				// On r�cup les items en vente en fonction du type de l'itme
				List<ShopItem> tmpSellItems = items.getItems(is);
				// Si on trouve un item alors on peut continuer
				if (tmpSellItems.size() != 0) {
					ShopItem item = tmpSellItems.get(0);

					// on initialise les variables temporaire
					double tmpPrice = item.getSellPrice();

					if (!item.isSellable())
						continue;

					int tmpAmount = is.getAmount();
					// On multiplie par le nombre d'item
					tmpPrice *= tmpAmount;
					// On modifie les varirables
					price += tmpPrice;
					// on ajoute l'item et le nombre d'item dans la map
					map.put(is, tmpAmount + map.getOrDefault(is, 0));
					eco = item.getEconomyType();
					// On retire l'item de l'inventaire du joueur
				}
			}
		}

		// On call l'event
		if (Config.shopPreSellAllEvent) {
			ShopPreSellAllEvent allEvent = new ShopPreSellAllEvent(player, map, price);
			Bukkit.getPluginManager().callEvent(allEvent);
			if (allEvent.isCancelled())
				return;

			map = allEvent.getItems();
			price = allEvent.getPrice();

		}

		// On cr�er le message pour donner les items vendu
		StringBuilder builder = new StringBuilder();
		AtomicInteger atomicInteger = new AtomicInteger();
		for (Entry<ItemStack, Integer> e : map.entrySet()) {
			ItemStack items = e.getKey();
			Integer amout = e.getValue();
			int tmp = atomicInteger.addAndGet(1);
			if (tmp == map.size())
				builder.append(" " + Lang.and + " ");
			else if (tmp != 1)
				builder.append(", ");
			builder.append(Lang.sellHandAllItem.replace("%amount%", String.valueOf(amout)).replace("%item%",
					getItemName(items)));
			player.getInventory().remove(items);
		}
		// On donne l'argent au mec
		depositMoney(player, price);
		// On envoie le message

		String str = Lang.sellHandAll.replace("%item%", builder.toString());
		str = str.replace("%currency%", eco.toCurrency());
		message(player, str.replace("%price%", String.valueOf(price)));

		// On call l'event
		if (Config.shopPostSellAllEvent) {
			ShopPostSellAllEvent allEvent = new ShopPostSellAllEvent(player, map, price);
			Bukkit.getPluginManager().callEvent(allEvent);
		}

	}

	@Override
	public CommandType openShop(Player player, String str) {

		CommandManager commandManager = plugin.getCommandManager();
		for (VCommand command : commandManager.getCommands())
			if (command.getSubCommands().contains(str.toLowerCase()))
				return CommandType.CONTINUE;

		Category category = plugin.getCategories().getCategory(str);

		if (category == null)
			return CommandType.SYNTAX_ERROR;

		this.openShop(player, EnumCategory.SHOP, 1, 1, Permission.SHOP_OPEN.getPermission(category.getId()), category);

		return CommandType.SUCCESS;
	}

	@Override
	public void openShop(Player player, EnumCategory category, int page, int id, Permission permission,
			Object... args) {
		this.openShop(player, category, page, id, permission.getPermission(), args);
	}

	@Override
	public void openShop(Player player, Command command) {

		InventoryObject obj = inventories.getInventory(command);

		if (obj != null)
			this.openShop(player, EnumCategory.DEFAULT, 0, obj.getId(), Permission.SHOP_DEFAULT);
		else {
			message(player, "�cPlease contact an administrator for he perform command �f'�6/shop reload�f'");
		}

	}

	@Override
	public void openShopWithCitizen(Player player, NPC npc) {

		String name = npc.getName();
		int categoryId = Config.citizens.getOrDefault(name, -99);
		if (categoryId == -99)
			return;
		else if (categoryId == -1)
			this.openShop(player, EnumCategory.DEFAULT, 0, 1, Permission.SHOP_USE);
		else if (categoryId > 0) {
			Category category = plugin.getCategories().getCategory(categoryId);
			if (category == null)
				return;
			openShop(player, EnumCategory.SHOP, 1, 1, Permission.SHOP_OPEN.getPermission(category.getId()), category);
		}

	}

	@Override
	public CommandType openConfigShop(Player player, String str, int page) {

		CommandManager commandManager = plugin.getCommandManager();
		for (VCommand command : commandManager.getCommands())
			if (command.getSubCommands().contains(str.toLowerCase()))
				return CommandType.CONTINUE;

		Category category = plugin.getCategories().getCategory(str);

		if (category == null)
			return CommandType.SYNTAX_ERROR;

		if (category.getType().equals(ShopType.ITEM)) {
			message(player, Lang.shopConfigError);
			return CommandType.SUCCESS;
		}

		this.openShop(player, EnumCategory.CONFIG, page, 1, Permission.SHOP_OPEN.getPermission(category.getId()),
				category);

		return CommandType.SUCCESS;

	}

}
