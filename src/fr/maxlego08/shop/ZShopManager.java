package fr.maxlego08.shop;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.CommandObject;
import fr.maxlego08.shop.command.commands.CommandInventory;
import fr.maxlego08.shop.inventory.InventoryManager;
import fr.maxlego08.shop.zcore.utils.yaml.YamlUtils;

public class ZShopManager extends YamlUtils implements ShopManager {

	private final ZShop plugin;

	public ZShopManager(ZShop plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public void loadCommands() throws Exception {

		FileConfiguration config = getConfig();

		ConfigurationSection section = config.getConfigurationSection("commands.");

		CommandManager commandManager = plugin.getCommandManager();
		for (String key : section.getKeys(false)) {

			String path = "commands." + key + ".";

			String stringCommand = config.getString(path + ".command");
			List<String> aliases = config.getStringList(path + "aliases");
			String stringInventory = config.getString(path + "inventory");
			String permission = config.getString(path + "permission", null);
			String description = config.getString(path + "description", null);

			Inventory inventory = plugin.getInventory().loadInventory(stringInventory);

			Command command = new CommandObject(stringCommand, aliases, inventory, permission, description);
			commandManager.registerCommand(stringCommand, new CommandInventory(command), aliases);

			success("Register command /" + stringCommand);

		}

	}

	@Override
	public void open(Player player, Command command) {

		Inventory inventory = command.getInventory();
		
		InventoryManager inventoryManager = plugin.getInventoryManager();
		inventoryManager.createInventory(fr.maxlego08.shop.zcore.enums.EnumInventory.INVENTORY_DEFAULT, player, 1,
				inventory, null);

	}

}
