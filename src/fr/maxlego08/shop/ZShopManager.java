package fr.maxlego08.shop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.ItemConfirmDoubleButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.command.OptionalCommand;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.enums.OptionalAction;
import fr.maxlego08.shop.api.enums.PermissionType;
import fr.maxlego08.shop.api.events.ZShopInventoryOpen;
import fr.maxlego08.shop.api.exceptions.InventoryNotFoundException;
import fr.maxlego08.shop.api.history.HistoryManager;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.api.permission.Permission;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.CommandObject;
import fr.maxlego08.shop.command.commands.CommandInventory;
import fr.maxlego08.shop.inventory.InventoryManager;
import fr.maxlego08.shop.permission.ZPermission;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.enums.Message;
import fr.maxlego08.shop.zcore.utils.TemporyObject;
import fr.maxlego08.shop.zcore.utils.itemstack.NMSUtils;
import fr.maxlego08.shop.zcore.utils.yaml.YamlUtils;

public class ZShopManager extends YamlUtils implements ShopManager {

	private final ZShop plugin;
	private final IEconomy economy;
	private final List<Permission> permissions = new ArrayList<>();
	private final Map<UUID, TemporyObject> tmpObjects = new HashMap<>();
	private boolean registerCommandInSpigot = true;
	private Economy defaultEconomy = Economy.VAULT;

	public ZShopManager(ZShop plugin, IEconomy economy) {
		super(plugin);
		this.plugin = plugin;
		this.economy = economy;
	}

	@Override
	public void loadCommands() throws Exception {

		FileConfiguration config = getConfig();

		permissions.clear();

		if (config.getConfigurationSection("boostSellPermission") != null && config.getBoolean("useSellPermission")) {

			for (String value : config.getConfigurationSection("boostSellPermission.").getKeys(false)) {

				double percent = Double.valueOf(value);
				String permissionString = config.getString("boostSellPermission." + percent);
				Permission permission = new ZPermission(PermissionType.SELL, percent, permissionString);
				permissions.add(permission);

			}

		}

		if (config.getConfigurationSection("boostBuyPermission") != null && config.getBoolean("useBuyPermission")) {

			for (String value : config.getConfigurationSection("boostBuyPermission.").getKeys(false)) {

				double percent = Double.valueOf(value);
				String permissionString = config.getString("boostBuyPermission." + percent);
				Permission permission = new ZPermission(PermissionType.BUY, percent, permissionString);
				permissions.add(permission);

			}

		}

		registerCommandInSpigot = config.getBoolean("registerCommandInSpigot", true);

		success("Loaded " + permissions.size() + " permissions");

		ConfigurationSection section = config.getConfigurationSection("commands.");

		CommandManager commandManager = plugin.getCommandManager();
		commandManager.clear();

		for (String key : section.getKeys(false)) {

			String path = "commands." + key + ".";

			String stringCommand = config.getString(path + ".command");
			List<String> aliases = config.getStringList(path + "aliases");
			String stringInventory = config.getString(path + "inventory");
			String permission = config.getString(path + "permission", null);
			String description = config.getString(path + "description", null);

			Inventory inventory = plugin.getInventory().loadInventory(stringInventory);

			Map<OptionalAction, OptionalCommand> commands = new HashMap<>();

			if (config.getConfigurationSection(path + "options.") != null)
				for (String option : config.getConfigurationSection(path + "options.").getKeys(false)) {

					String tmpKey = path + "options." + option + ".";

					OptionalAction action = OptionalAction.valueOf(config.getString(tmpKey + "action").toUpperCase());
					String optionPermission = config.getString(tmpKey + "permission", null);
					String optionDescription = config.getString(tmpKey + "description", null);

					OptionalCommand command = new OptionalCommand(action, optionPermission, optionDescription);
					commands.put(action, command);

				}

			Command command = new CommandObject(stringCommand, aliases, inventory, permission, description, commands);
			commandManager.registerCommand(stringCommand, new CommandInventory(plugin.getCommandManager(), command),
					aliases, registerCommandInSpigot);

			success("Register command /" + stringCommand);

		}

	}

	@Override
	public void open(Player player, Command command) {

		Inventory inventory = command.getInventory();

		ZShopInventoryOpen event = new ZShopInventoryOpen(inventory, command, player);
		event.callEvent();

		if (event.isCancelled()) {
			return;
		}

		InventoryManager inventoryManager = this.plugin.getInventoryManager();
		inventoryManager.createInventory(EnumInventory.INVENTORY_DEFAULT, player, 1, inventory, new ArrayList<>(),
				command);

	}

	@Override
	public void open(Player player, Command command, String category) {

		if (category == null) {
			message(player, Message.CATEGORY_EMPTY);
			return;
		}

		Optional<Inventory> optional = getInventoryByName(category);

		if (!optional.isPresent()) {
			message(player, Message.CATEGORY_DOESNT_EXIST, "%name%", category);
			return;
		}

		Inventory inventory = optional.get();

		List<Inventory> list = new ArrayList<>();
		list.add(command.getInventory());

		ZShopInventoryOpen event = new ZShopInventoryOpen(inventory, command, player);
		event.callEvent();

		if (event.isCancelled()) {
			return;
		}

		InventoryManager inventoryManager = plugin.getInventoryManager();
		inventoryManager.createInventory(EnumInventory.INVENTORY_DEFAULT, player, 1, inventory, list, command);
	}

	@Override
	public void reload() {

		// Unregister commands

		long ms = System.currentTimeMillis();

		info("Reload starting...");

		info("Closure of all inventories...");
		try {
			closeInventory();
		} catch (Exception e) {
		}

		plugin.getSavers().forEach(saver -> saver.load(this.plugin.getPersist()));

		info("Deleting commands...");
		FileConfiguration configuration = getConfig();

		ConfigurationSection section = configuration.getConfigurationSection("commands.");

		for (String key : section.getKeys(false)) {

			String path = "commands." + key + ".";
			String stringCommand = configuration.getString(path + ".command");

			PluginCommand command = plugin.getCommand(stringCommand);
			if (command != null)
				unRegisterBukkitCommand(command);

		}

		info("Deleting inventories...");
		plugin.getInventory().delete();

		info("Reload config file");
		plugin.reloadConfig();

		FileConfiguration config = getConfig();
		try {
			this.defaultEconomy = Economy
					.valueOf(config.getString("defaultEconomy", Economy.VAULT.name()).toUpperCase());
			success("Default Economy: �7" + defaultEconomy.name());
		} catch (Exception e) {
			error("Could not find " + config.getString("defaultEconomy") + " economy");
		}

		/* Load inventories */
		try {
			plugin.getInventory().loadInventories();
		} catch (Exception e) {
			e.printStackTrace();
			// plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		/* Load Commands */
		try {
			loadCommands();
		} catch (Exception e) {
			e.printStackTrace();
			// plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		ms = System.currentTimeMillis() - ms;
		info("Reload done (" + ms + " ms)");

	}

	private Object getPrivateField(Object object, String field)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		java.lang.reflect.Field objectField = field.equals("commandMap") ? clazz.getDeclaredField(field)
				: field.equals("knownCommands") ? NMSUtils.isNewVersion()
						? clazz.getSuperclass().getDeclaredField(field) : clazz.getDeclaredField(field) : null;
		objectField.setAccessible(true);
		Object result = objectField.get(object);
		objectField.setAccessible(false);
		return result;
	}

	private void unRegisterBukkitCommand(PluginCommand cmd) {
		try {
			Object result = getPrivateField(plugin.getServer().getPluginManager(), "commandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) result;

			Object map = getPrivateField(commandMap, "knownCommands");
			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
			knownCommands.remove(cmd.getName());
			for (String alias : cmd.getAliases())
				knownCommands.remove(alias);
			knownCommands.remove("zshop:" + cmd.getName());
			for (String alias : cmd.getAliases())
				knownCommands.remove("zshop:" + alias);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeInventory() {
		plugin.getInventoryManager().close();
	}

	@Override
	public IEconomy getIEconomy() {
		return economy;
	}

	@Override
	public void open(Player player, Command command, ItemButton button, int page, List<Inventory> oldInventories,
			InventoryType type) {

		Inventory typeInventory = plugin.getInventory().getInventory(type);

		if (typeInventory == null)
			throw new InventoryNotFoundException("Cannot find the inventory with the type " + type);

		ZShopInventoryOpen event = new ZShopInventoryOpen(typeInventory, command, player);
		event.callEvent();

		if (event.isCancelled()) {
			return;
		}

		switch (type) {
		case BUY:
		case SELL:
			plugin.getInventoryManager().createInventory(EnumInventory.INVENTORY_SHOP, player, 1, typeInventory, button,
					oldInventories, page, command, type);
			break;
		case CONFIRM:
			plugin.getInventoryManager().createInventory(EnumInventory.INVENTORY_CONFIRM, player, 1, typeInventory,
					button, oldInventories, page, command);
			break;
		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void sellHand(Player player, int amount) {

		if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
			message(player, Message.SELLHAND_AIR);
			return;
		}

		Optional<ItemButton> optional = getItemButton(player.getItemInHand());

		if (!optional.isPresent())
			message(player, Message.SELLHAND_EMPTY);
		else {
			ItemButton button = optional.get();

			if (button.canSell()) {
				button.sell(player, amount);
			} else {
				message(player, Message.SELLHAND_EMPTY);
			}
		}

	}

	@Override
	public void sellAllHand(Player player) {
		this.sellHand(player, 0);
	}

	@Override
	public void sellAll(Player player) {

		double price = 0;
		Map<ItemStack, Integer> map = new HashMap<ItemStack, Integer>();
		Economy economy = null;
		PlayerInventory inventory = player.getInventory();

		// On parcours l'inventaire du joueur
		for (int slot = 0; slot != 36; slot++) {

			ItemStack itemStack = inventory.getContents()[slot];

			// On verif si l'item est pas null
			if (itemStack != null) {

				Optional<ItemButton> optional = getItemButton(itemStack);

				if (optional.isPresent()) {

					ItemButton button = optional.get();

					double tmpPrice = button.getSellPrice(player);

					if (tmpPrice <= 0){
						continue;
					}

					if (economy != null && !economy.equals(button.getEconomy())) {
						continue;
					}

					int tmpAmount = itemStack.getAmount();

					// On multiplie par le nombre d'item
					tmpPrice *= tmpAmount;
					// On modifie les varirables
					price += tmpPrice;
					// on ajoute l'item et le nombre d'item dans la map
					map.put(itemStack, tmpAmount + map.getOrDefault(itemStack, 0));

					economy = button.getEconomy();

					// On retire l'item de l'inventaire du joueur
					inventory.setItem(slot, null);

				}

			}
		}

		if (economy == null) {
			message(player, Message.SELLALL_ERROR);
			return;

		}

		StringBuilder builder = new StringBuilder();
		AtomicInteger atomicInteger = new AtomicInteger();
		for (Entry<ItemStack, Integer> e : map.entrySet()) {
			ItemStack items = e.getKey();
			Integer amout = e.getValue();
			int tmp = atomicInteger.addAndGet(1);
			if (tmp == map.size())
				builder.append(" " + Message.AND + " ");
			else if (tmp != 1)
				builder.append(", ");
			String message = Message.SELLHAND_ALLITEM.replace("%amount%", String.valueOf(amout)).replace("%item%",
					getItemName(items));
			builder.append(message);
		}

		this.economy.depositMoney(economy, player, price);

		message(player, Message.SELLHAND_ALL, "%price%", format(price), "%item%", builder.toString(), "%currency%",
				economy.getCurrenry());

	}

	@Override
	public Optional<ItemButton> getItemButton(ItemStack itemStack) {
		return plugin.getInventory().getItemButton(itemStack);
	}

	@Override
	public Optional<Permission> getPermission(String permission) {
		return permissions.stream().filter(perm -> perm.getPermission().equals(permission)).findFirst();
	}

	@Override
	public Optional<Permission> getPermission(Player player, PermissionType type) {

		if (this.tmpObjects.containsKey(player.getUniqueId())) {
			TemporyObject object = this.tmpObjects.get(player.getUniqueId());
			if (!object.isExpired())
				return object.getPermission();
		}

		Optional<Permission> optional = permissions.stream()
				.filter(perm -> perm.getType().equals(type) && player.hasPermission(perm.getPermission()))
				.sorted(Comparator.comparingDouble(Permission::getPercent).reversed()).findFirst();

		this.tmpObjects.put(player.getUniqueId(), new TemporyObject(optional));
		return optional;
	}

	@Override
	public Optional<Inventory> getInventoryByName(String name) {
		return plugin.getInventory().getInventoryByName(name);
	}

	@Override
	public HistoryManager getHistory() {
		return plugin.getHistoryManager();
	}

	@Override
	public void open(Player player, Command command, ItemConfirmDoubleButton button, int page,
			List<Inventory> oldInventories, boolean isRight) {

		InventoryType type = InventoryType.CONFIRM;
		Inventory typeInventory = plugin.getInventory().getInventory(type);

		if (typeInventory == null)
			throw new InventoryNotFoundException("Cannot find the inventory with the type " + type);

		ZShopInventoryOpen event = new ZShopInventoryOpen(typeInventory, command, player);
		event.callEvent();

		if (event.isCancelled()) {
			return;
		}

		this.plugin.getInventoryManager().createInventory(EnumInventory.INVENTORY_CONFIRM, player, 1, typeInventory,
				button, oldInventories, page, command, isRight);
	}

	@Override
	public Economy getDefaultEconomy() {
		return this.defaultEconomy;
	}

}
