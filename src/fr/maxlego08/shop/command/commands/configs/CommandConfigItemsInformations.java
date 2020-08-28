package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandConfigItemsInformations extends VCommand {

	public CommandConfigItemsInformations() {
		this.addSubCommand("items");
		this.setPermission(Permission.SHOP_CONFIG_ITEMS);
		this.setShowHelp(false);
		this.addRequireArg("category id");
		this.setDescription("Show items informations form category");
		this.setConsoleCanUse(false);
	}

	@Override
	protected CommandType perform(ZShop main) {
		items.showInformation(player, categories.getCategory(argAsInteger(0)));
		return CommandType.SUCCESS;
	}

}
