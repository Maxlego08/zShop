package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.categories.Category;

public class CommandConfigEditItem extends VCommand {

	public CommandConfigEditItem() {
		this.addSubCommand("edit");
		this.setPermission(Permission.SHOP_CONFIG_EDIT_ITEM);
		this.addRequireArg("sell/buy");
		this.addRequireArg("category id");
		this.addRequireArg("item id");
		this.addRequireArg("new price");
		this.setDescription("Edit price");
		this.setShowHelp(false);
		this.DEBUG = true;
	}

	@Override
	protected CommandType perform(ZShop main) {
		
		String type = argAsString(0);
		int id = argAsInteger(1);
		int itemId = argAsInteger(2);
		double price = argAsDouble(3);
		
		if (!type.equalsIgnoreCase("buy") && !type.equalsIgnoreCase("sell"))
			return CommandType.SYNTAX_ERROR;
		
		Category category = categories.getCategory(id);

		if (category == null) {
			message(sender, Lang.commandCategoryError.replace("%id%", String.valueOf(id)));
			return CommandType.DEFAULT;
		}
		
		items.updatePrice(sender, type.equalsIgnoreCase("sell"), category, itemId, price);
		
		return CommandType.SUCCESS;
	}

}
