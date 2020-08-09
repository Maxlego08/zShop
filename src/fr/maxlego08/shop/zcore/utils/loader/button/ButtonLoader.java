package fr.maxlego08.shop.zcore.utils.loader.button;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.Loader;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.button.buttons.ZBackButton;
import fr.maxlego08.shop.button.buttons.ZHomeButton;
import fr.maxlego08.shop.button.buttons.ZInventoryButton;
import fr.maxlego08.shop.button.buttons.ZItemButton;
import fr.maxlego08.shop.button.buttons.ZPermissibleButton;
import fr.maxlego08.shop.zcore.utils.loader.ItemStackLoader;

public class ButtonLoader implements Loader<Button> {

	@Override
	public Button load(YamlConfiguration configuration, String path, Object... args) throws Exception {

		Loader<ItemStack> loaderItemStack = new ItemStackLoader();
		ButtonType type = ButtonType.from(configuration.getString(path + "type"), (String) args[0], path + "type");
		int slot = configuration.getInt(path + "slot");
		ItemStack itemStack = loaderItemStack.load(configuration, path + "item.");

		// Permission
		String permission = configuration.getString(path + "permission", null);
		Button elseButton = null;
		String elseMessage = configuration.getString(path + "elseMessage", null);
		if (permission != null && args.length < 2)
			if (configuration.contains(path + "else"))
				elseButton = load(configuration, path + "else.", (String) args[0], true);

		if (args.length >= 2) {
			permission = null;
			elseButton = null;
			elseMessage = null;
		}

		Button button = null;

		switch (type) {
		case BACK:
			String inventory = configuration.getString(path + "inventory");
			return new ZBackButton(type, itemStack, slot, inventory);
		case HOME:
			inventory = configuration.getString(path + "inventory");
			return new ZHomeButton(type, itemStack, slot, inventory);
		case INVENTORY:
			inventory = configuration.getString(path + "inventory");
			return new ZInventoryButton(type, itemStack, slot, inventory);
		case ITEM:
		case ITEM_CONFIRM:
			double sellPrice = configuration.getDouble(path + "sellPrice", 0.0);
			double buyPrice = configuration.getDouble(path + "buyPrice", 0.0);
			Economy economy = Economy.get(configuration.getString(path + "economy", null));
			return new ZItemButton(type, itemStack, slot, permission, elseMessage, elseButton, sellPrice, buyPrice,
					economy);
		case NEXT:
		case NONE:
		case PREVIOUS:
		default:
			button = new ZPermissibleButton(type, itemStack, slot, permission, elseMessage, elseButton);
		}

		return button;

	}

	@Override
	public void save(Button object, YamlConfiguration configuration, String path) {
		// TODO Auto-generated method stub

	}

}
