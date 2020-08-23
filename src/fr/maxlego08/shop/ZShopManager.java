package fr.maxlego08.shop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.exceptions.InventoryNotFoundException;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.CommandObject;
import fr.maxlego08.shop.command.commands.CommandInventory;
import fr.maxlego08.shop.inventory.InventoryManager;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.utils.ItemDecoder;
import fr.maxlego08.shop.zcore.utils.yaml.YamlUtils;

public class ZShopManager extends YamlUtils implements ShopManager {

	private final ZShop plugin;
	private final IEconomy economy;

	public ZShopManager(ZShop plugin, IEconomy economy) {
		super(plugin);
		this.plugin = plugin;
		this.economy = economy;
	}

	@Override
	public void loadCommands() throws Exception {

		FileConfiguration config = getConfig();

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

			Command command = new CommandObject(stringCommand, aliases, inventory, permission, description);
			commandManager.registerCommand(stringCommand, new CommandInventory(plugin.getCommandManager(), command),
					aliases);

			success("Register command /" + stringCommand);

		}

	}

	@Override
	public void open(Player player, Command command) {

		Inventory inventory = command.getInventory();

		InventoryManager inventoryManager = plugin.getInventoryManager();
		inventoryManager.createInventory(fr.maxlego08.shop.zcore.enums.EnumInventory.INVENTORY_DEFAULT, player, 1,
				inventory, new ArrayList<>(), command);

	}

	@Override
	public void reload() {

		// Unregister commands

		long ms = System.currentTimeMillis();

		info("Reload starting...");

		info("Closure of all inventories...");
		closeInventory();

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

		/* Load inventories */
		try {
			plugin.getInventory().loadInventories();
		} catch (Exception e) {
			e.printStackTrace();
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		/* Load Commands */
		try {
			loadCommands();
		} catch (Exception e) {
			e.printStackTrace();
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		ms = System.currentTimeMillis() - ms;
		info("Reload done (" + ms + " ms)");

	}

	private Object getPrivateField(Object object, String field)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		Field objectField = field.equals("commandMap") ? clazz.getDeclaredField(field)
				: field.equals("knownCommands") ? ItemDecoder.isNewVersion()
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

}
