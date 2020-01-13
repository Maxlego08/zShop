package fr.maxlego08.shop.zshop.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.exceptions.ItemCreateException;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.enums.Message;
import fr.maxlego08.shop.zshop.categories.Category;
import fr.maxlego08.shop.zshop.factories.Items;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;
import fr.maxlego08.shop.zshop.utils.ItemStackYAMLoader;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ShopItemManager extends ZUtils implements Items {

	private final ZShop plugin;

	private Map<Integer, List<ShopItem>> items = new HashMap<Integer, List<ShopItem>>();

	public ShopItemManager(ZShop plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public List<ShopItem> getItems(int category) {
		return items.getOrDefault(category, new ArrayList<>());
	}

	@Override
	public void load() {
		File file = new File(plugin.getDataFolder() + File.separator + "items.yml");
		if (!file.exists())
			return;
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

		if (configuration.getString("items") == null) {
			Logger.info("Impossible de charger les items !", LogType.ERROR);
			return;
		}

		int itemAmount = 0;

		ItemStackYAMLoader itemStackYAMLoader = new ItemStackYAMLoader();

		// Chargement des categories
		for (String categoryId : configuration.getConfigurationSection("items.").getKeys(false)) {

			List<ShopItem> currentItems = new ArrayList<>();
			int currentId = 0;

			try {
				currentId = Integer.valueOf(categoryId);
			} catch (NumberFormatException e) {
				Logger.info("La catégorie " + categoryId + " n'est pas un nombre !", LogType.ERROR);
				return;
			}

			// Chargement des items
			for (String itemId : configuration.getConfigurationSection("items." + categoryId + ".items.")
					.getKeys(false)) {

				try {

					// Chargement des éléments de l'item

					String currentPath = "items." + categoryId + ".items." + itemId + ".";

					ShopType type = ShopType.valueOf(configuration.getString(currentPath + "type").toUpperCase());

					if (type == null) {
						throw new ItemCreateException("Could load item with id " + itemId + " in category " + categoryId
								+ " ! ShopType is null ! Set " + ShopType.UNIQUE_ITEM.name() + " or "
								+ ShopType.ITEM.name() + " !");
					}

					ItemStack itemStack = itemStackYAMLoader.load(configuration, currentPath + ".item.");
					ItemStack giveItemStack = itemStackYAMLoader.load(configuration, currentPath + ".giveItem.");

					int slot = configuration.getInt(currentPath + "slot", 0);
					double sellPrice = configuration.getDouble(currentPath + "sellPrice", 0);
					double buyPrice = configuration.getDouble(currentPath + "buyPrice", 0);
					Economy economy = Economy.getOrDefault(configuration.getString(currentPath + "economy", "VAULT"),
							Economy.VAULT);
					boolean giveItem = configuration.getBoolean(currentPath + "give", true);
					boolean executeSellCommand = configuration.getBoolean(currentPath + "executeSellCommand", true);
					boolean executeBuyCommand = configuration.getBoolean(currentPath + "executeBuyCommand", true);
					boolean useConfirm = configuration.getBoolean(currentPath + "useConfirm", true);
					int maxStackSize = configuration.getInt(currentPath + "item.stack", 64);
					List<String> commands = configuration.getStringList(currentPath + "commands");

					ShopItem item = null;
					switch (type) {
					case ITEM: {
						item = new ShopItemConsomable(economy, currentId, itemStack, sellPrice, buyPrice, maxStackSize,
								giveItem, executeSellCommand, executeBuyCommand, commands);
						break;
					}
					case UNIQUE_ITEM: {
						item = new ShopItemUnique(economy, currentId, slot, itemStack, buyPrice, useConfirm, commands,
								giveItemStack);
						break;
					}
					}

					currentItems.add(item);

				} catch (Exception e) {

					// S'il y a une erreur alors il est impossible de charger
					// l'item

					try {
						throw new ItemCreateException(
								"Could load item with id " + itemId + " in category " + categoryId);
					} catch (ItemCreateException e1) {
						e1.printStackTrace();
					}
				}
			}

			// Ajout des items

			this.items.put(currentId, currentItems);
			itemAmount += currentItems.size();
		}

		Logger.info(file.getAbsolutePath() + " loaded successfully !", LogType.SUCCESS);
		Logger.info("Loading " + itemAmount + " items", LogType.SUCCESS);
	}

	@Override
	public void setItems(List<ShopItem> items, int id) {
		this.items.put(id, items);
	}

	@Override
	public void save(String fileName) {

		if (items == null)
			throw new IllegalArgumentException("La liste des items est null !");

		File file = new File(plugin.getDataFolder() + File.separator + fileName + ".yml");
		if (file.exists())
			file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		ItemStackYAMLoader stackYAMLoader = new ItemStackYAMLoader();
		this.items.forEach((id, items) -> {
			AtomicInteger integer = new AtomicInteger(1);
			items.forEach(item -> {
				String path = "items." + id + ".items." + integer.getAndIncrement() + ".";
				configuration.set(path + "type", item.getType().name());
				stackYAMLoader.save(item.getType().equals(ShopType.ITEM) ? item.getItem() : item.getDisplayItem(),
						configuration, path + "item.");
				if (item.getMaxStackSize() != 64)
					configuration.set(path + "item.stack", item.getMaxStackSize());
				configuration.set(path + "buyPrice", item.getSellPrice());
				configuration.set(path + "sellPrice", item.getBuyPrice());
				configuration.set(path + "economy", item.getEconomyType());
				if (item instanceof ShopItemConsomable) {
					ShopItemConsomable tmpItem = (ShopItemConsomable) item;
					configuration.set(path + "give", tmpItem.isGiveItem());
					configuration.set(path + "executeSellCommand", tmpItem.isExecuteSellCommand());
					configuration.set(path + "executeBuyCommand", tmpItem.isExecuteBuyCommand());
					configuration.set(path + "commands", tmpItem.getCommands());
				} else {
					ShopItemUnique tmpItem = (ShopItemUnique) item;
					stackYAMLoader.save(tmpItem.getItem(), configuration, path + "giveItem.");
				}
			});
		});
		try {
			configuration.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ShopItem> getItems(ItemStack itemStack) {
		List<ShopItem> items = new ArrayList<>();
		if (itemStack == null)
			return items;
		this.items.values()
				.forEach(list -> items.addAll(
						list.stream().filter(item -> item.getItem() != null && item.getItem().isSimilar(itemStack))
								.collect(Collectors.toList())));
		return items;
	}

	@Override
	public boolean hasItems(ItemStack itemStack) {
		return getItems(itemStack).size() != 0;
	}

	@Override
	public void addItem(CommandSender sender, Category category, double sellPrice, double buyPrice, MaterialData data,
			int maxStackSize) {

		ItemStack itemStack = data.toItemStack();

		if (category.getType().equals(ShopType.UNIQUE_ITEM)) {
			message(sender, Lang.configAddItemErrorCategory);
			return;
		}

		// On verifie si l'item existe ou pas avant
		if (hasItems(category, itemStack)) {
			message(sender, Lang.configAddItemError.replace("%item%", getItemName(itemStack)));
			return;
		}

		ShopItem shopItem = new ShopItemConsomable(category.getId(), itemStack, sellPrice, buyPrice, maxStackSize);
		items.get(category.getId()).add(shopItem);
		this.save("items");
		message(sender, Lang.configAddItemSuccess.replace("%item%", getItemName(itemStack)).replace("%category%",
				category.getName()));

	}

	@Override
	public boolean hasItems(Category category, ItemStack itemStack) {
		List<ShopItem> items = getItems(itemStack);
		if (items.size() == 0)
			return false;
		return items.stream().filter(item -> item.getCategory() == category.getId()).findAny().isPresent();
	}

	@Override
	public void removeItem(CommandSender sender, Category category, MaterialData data) {

		ItemStack itemStack = data.toItemStack();

		if (category.getType().equals(ShopType.UNIQUE_ITEM)) {
			message(sender, Lang.configAddItemErrorCategory);
			return;
		}

		// On verifie si l'item existe ou pas avant
		if (!hasItems(category, itemStack)) {
			message(sender, Lang.configRemoveItemError.replace("%item%", getItemName(itemStack)));
			return;
		}

		ShopItem item = getItem(category, itemStack);

		if (item == null) {
			message(sender, Lang.configRemoveItemError.replace("%item%", getItemName(itemStack)));
			return;
		}

		items.get(category.getId()).remove(item);

		this.save("items");
		message(sender, Lang.configRemoveItemSuccess.replace("%item%", getItemName(itemStack)).replace("%category%",
				category.getName()));

	}

	@Override
	public void updatePrice(CommandSender sender, boolean isSell, Category category, int id, double price) {

		if (category.getType().equals(ShopType.UNIQUE_ITEM)) {
			message(sender, Lang.configAddItemErrorCategory);
			return;
		}

		ShopItemConsomable item = (ShopItemConsomable) getItem(category, id);

		if (item == null) {
			message(sender, Lang.configEditError);
			return;
		}

		if (isSell)
			item.setSellPrice(price);
		else
			item.setBuyPrice(price);

		message(sender,
				Lang.configEditSuccess.replace("%price%", format(price)).replace("%item%", getItemName(item.getItem()))
						.replace("%type%", isSell ? Lang.boostSell : Lang.boostBuy));

		schedule(1, () -> save("items"));

	}

	@Override
	public ShopItem getItem(Category category, ItemStack itemStack) {
		List<ShopItem> items = getItems(itemStack);
		if (items.size() == 0)
			return null;
		return items.stream().filter(item -> item.getCategory() == category.getId()).findAny().orElse(null);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void showInformation(Player player, Category category) {

		if (category.getType().equals(ShopType.UNIQUE_ITEM)) {
			message(player, Lang.configAddItemErrorCategory);
			return;
		}

		// Alors la c'est compliqué quand même
		List<ShopItem> items = getItems(category.getId());
		message(player, Message.ITEMS_INFORMATIONS);
		AtomicInteger itemId = new AtomicInteger(1);
		items.forEach(item -> {

			int id = itemId.getAndIncrement();

			TextComponent message = new TextComponent(
					Lang.prefix + " §eItem§8: §6" + getItemName(item.getItem()) + " §b» ");
			BaseComponent[] components = { new TextComponent("§7§m-----------------------\n"),
					new TextComponent("§f§l» §7Id: §2" + id + "\n"),
					new TextComponent("§f§l» §7Item: §2" + getItemName(item.getItem()) + "\n"),
					new TextComponent("§f§l» §7Item data: §2" + item.getItem().getData().getData() + "\n"),
					new TextComponent("§f§l» §7Sell price: §2" + format(item.getSellPrice()) + "$\n"),
					new TextComponent("§f§l» §7Buy price: §2" + format(item.getBuyPrice()) + "$\n"),
					new TextComponent("§f§l» §7Max stack size: §2" + item.getMaxStackSize() + "\n"),
					new TextComponent("§7§m-----------------------") };
			HoverEvent event = new HoverEvent(Action.SHOW_TEXT, components);

			TextComponent delete = new TextComponent("§cdelete");
			delete.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND,
					"/zshop config remove " + category.getId() + " " + item.getItem().getData().getItemType().getId()
							+ ":" + item.getItem().getData().getData()));
			delete.setHoverEvent(
					new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("§cDelete me") }));
			message.addExtra(delete);

			message.addExtra(" §7» ");

			TextComponent editSellPrice = new TextComponent("§aedit sellPrice");
			editSellPrice.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND,
					"/zshop config edit sell " + category.getId() + " " + id + " " + item.getSellPrice()));
			editSellPrice.setHoverEvent(
					new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("§aUpdate sell price") }));
			message.addExtra(editSellPrice);

			message.addExtra(" §7» ");

			TextComponent editBuyPrice = new TextComponent("§2edit buyPrice");
			editBuyPrice.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND,
					"/zshop config edit buy " + category.getId() + " " + id + " " + item.getBuyPrice()));
			editBuyPrice.setHoverEvent(
					new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("§2Update buy price") }));
			message.addExtra(editBuyPrice);

			message.setHoverEvent(event);

			player.spigot().sendMessage(message);

		});

	}

	@Override
	public ShopItem getItem(Category category, int itemId) {
		List<ShopItem> items = getItems(category.getId());
		int tmpId = 1;
		for (ShopItem item : items) {
			if (tmpId == itemId)
				return item;
			tmpId++;
		}
		return null;
	}

}
