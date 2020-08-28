package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandConfigCategoriesInformations extends VCommand {

	public CommandConfigCategoriesInformations() {
		this.addSubCommand("categories", "c");
		this.setPermission(Permission.SHOP_CONFIG_CATEGORIES);
		this.setShowHelp(false);
		this.setDescription("Show categories informations");
		this.setConsoleCanUse(false);
	}

	@Override
	protected CommandType perform(ZShop main) {
		categories.showInformation(player);
		return CommandType.SUCCESS;
	}

}
