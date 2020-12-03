package fr.maxlego08.shop.zcore.utils.loader.button;

import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.Loader;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.enums.ZSpawnerAction;
import fr.maxlego08.shop.api.exceptions.ButtonCreateItemStackNullPointerException;
import fr.maxlego08.shop.api.sound.SoundOption;
import fr.maxlego08.shop.button.buttons.ZAddRemoveButton;
import fr.maxlego08.shop.button.buttons.ZBackButton;
import fr.maxlego08.shop.button.buttons.ZButtonSlot;
import fr.maxlego08.shop.button.buttons.ZHomeButton;
import fr.maxlego08.shop.button.buttons.ZInventoryButton;
import fr.maxlego08.shop.button.buttons.ZItemButton;
import fr.maxlego08.shop.button.buttons.ZMoveButton;
import fr.maxlego08.shop.button.buttons.ZPerformButton;
import fr.maxlego08.shop.button.buttons.ZPlaceholderButton;
import fr.maxlego08.shop.button.buttons.ZShowButton;
import fr.maxlego08.shop.button.buttons.ZZSpawnerButton;
import fr.maxlego08.shop.sound.ZSoundOption;
import fr.maxlego08.shop.zcore.utils.XSound;
import fr.maxlego08.shop.zcore.utils.loader.ItemStackLoader;

public class ButtonLoader implements Loader<Button> {

	private final ZShop plugin;
	private final IEconomy economy;

	/**
	 * @param plugin
	 * @param economy
	 */
	public ButtonLoader(ZShop plugin, IEconomy economy) {
		super();
		this.plugin = plugin;
		this.economy = economy;
	}

	@Override
	public Button load(YamlConfiguration configuration, String path, Object... args) throws Exception {

		Loader<ItemStack> loaderItemStack = new ItemStackLoader();
		ButtonType type = ButtonType.from(configuration.getString(path + "type"), (String) args[0], path + "type");

		if (type.equals(ButtonType.ZSPAWNER) && Bukkit.getPluginManager().getPlugin("zSpawner") == null)
			type = ButtonType.NONE;

		int slot = configuration.getInt(path + "slot");
		boolean isPermanent = configuration.getBoolean(path + "isPermanent", false);
		boolean glowIfCheck = configuration.getBoolean(path + "glowIfCheck", false);
		slot = slot < 0 ? 0 : slot;

		String name = (String) args[0];

		ItemStack itemStack = loaderItemStack.load(configuration, path + "item.");

		if (itemStack == null && !type.isShow())
			throw new ButtonCreateItemStackNullPointerException(
					"Cannot find the itemtack for the button " + path + "item in inventory " + name);

		// Permission
		String permission = configuration.getString(path + "permission", null);
		Button elseButton = null;
		String elseMessage = configuration.getString(path + "elseMessage", null);

		PlaceholderAction action = PlaceholderAction.from(configuration.getString(path + "action", null));
		String placeHolder = configuration.getString(path + "placeHolder", null);
		double value = configuration.getDouble(path + "value", 0.0);

		// Sound

		Optional<XSound> optional = XSound.matchXSound(configuration.getString(path + "sound", null));
		XSound xSound = optional.isPresent() ? optional.get() : null;

		SoundOption sound = null;
		if (optional.isPresent()) {
			float pitch = Float.valueOf(configuration.getString(path + "pitch", "1.0f"));
			float volume = Float.valueOf(configuration.getString(path + "volume", "1.0f"));
			sound = new ZSoundOption(xSound, pitch, volume);
		}

		if (configuration.contains(path + "else"))
			elseButton = load(configuration, path + "else.", (String) args[0], true);

		Button button = null;

		switch (type) {
		case NEXT:
		case PREVIOUS:
			boolean display = configuration.getBoolean(path + "display", true);
			return new ZMoveButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, glowIfCheck, sound, display);
		case ADD:
		case REMOVE:
			int amount = configuration.getInt(path + "amount", 1);
			return new ZAddRemoveButton(type, itemStack, slot, amount, isPermanent, sound);
		case BACK:
			return new ZBackButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, null, null, plugin, glowIfCheck, sound);
		case HOME:
			return new ZHomeButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, null, null, plugin, glowIfCheck, sound);
		case INVENTORY:
			String inventory = configuration.getString(path + "inventory");
			return new ZInventoryButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, inventory, null, plugin, glowIfCheck, sound);
		case SHOW_ITEM: {
			List<String> lore = configuration.getStringList(path + "lore");
			return new ZShowButton(type, itemStack, slot, lore, isPermanent, sound);
		}
		case ITEM:
		case ITEM_CONFIRM: {
			double sellPrice = configuration.getDouble(path + "sellPrice", 0.0);
			double buyPrice = configuration.getDouble(path + "buyPrice", 0.0);
			int maxStack = configuration.getInt(path + "maxStack", 64);
			boolean giveItem = configuration.getBoolean(path + "giveItem", true);
			boolean log = configuration.getBoolean(path + "log", false);
			List<String> currentLore = configuration.getStringList(path + "lore");
			List<String> buyCommands = configuration.getStringList(path + "buyCommands");
			List<String> sellCommands = configuration.getStringList(path + "sellCommands");

			if (currentLore.size() == 0)
				currentLore = plugin.getInventory().getLore();

			Economy economy = Economy.get(configuration.getString(path + "economy", null));
			return new ZItemButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, plugin.getShopManager(), this.economy, sellPrice, buyPrice, maxStack, economy,
					currentLore, buyCommands, sellCommands, giveItem, glowIfCheck, log, sound);
		}
		case ZSPAWNER: {

			double sellPrice = configuration.getDouble(path + "sellPrice", 0.0);
			double buyPrice = configuration.getDouble(path + "buyPrice", 0.0);
			int maxStack = configuration.getInt(path + "maxStack", 64);
			boolean giveItem = configuration.getBoolean(path + "giveItem", true);
			boolean log = configuration.getBoolean(path + "log", false);
			List<String> currentLore = configuration.getStringList(path + "lore");
			List<String> buyCommands = configuration.getStringList(path + "buyCommands");
			List<String> sellCommands = configuration.getStringList(path + "sellCommands");

			if (currentLore.size() == 0)
				currentLore = plugin.getInventory().getLore();

			Economy economy = Economy.get(configuration.getString(path + "economy", null));

			EntityType entityType = EntityType.valueOf(configuration.getString(path + "entity").toUpperCase());
			ZSpawnerAction spawnerAction = ZSpawnerAction
					.valueOf(configuration.getString(path + "zpawnerAction").toUpperCase());
			int level = configuration.getInt(path + "level", 0);

			return new ZZSpawnerButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, plugin.getShopManager(), this.economy, sellPrice, buyPrice, maxStack, economy,
					currentLore, buyCommands, sellCommands, giveItem, entityType, spawnerAction, plugin, level,
					glowIfCheck, log, sound);
		}
		case PERFORM_COMMAND:
			List<String> commands = configuration.getStringList(path + "commands");
			List<String> consoleCommands = configuration.getStringList(path + "consoleCommands");
			List<String> consolePermissionCommands = configuration.getStringList(path + "consolePermissionCommands");
			String consolePermission = configuration.getString(path + "consolePermission");
			boolean closeInventory = configuration.getBoolean(path + "closeInventory", false);
			return new ZPerformButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, commands, consoleCommands, closeInventory, glowIfCheck, sound,
					consolePermissionCommands, consolePermission);
		case NONE_SLOT:
			List<Integer> list = configuration.getIntegerList(path + "slots");
			return new ZButtonSlot(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent, action,
					placeHolder, value, list, glowIfCheck, sound);
		case NONE:
		default:
			button = new ZPlaceholderButton(type, itemStack, slot, permission, elseMessage, elseButton, isPermanent,
					action, placeHolder, value, glowIfCheck, sound);
		}

		return button;

	}

	@Override
	public void save(Button object, YamlConfiguration configuration, String path) {
		// TODO Auto-generated method stub

	}

}
