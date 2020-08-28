package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopHelp extends VCommand {

	public CommandShopHelp() {
		this.addSubCommand("help", "aide", "?");
		this.setPermission(Permission.SHOP_HELP);
		this.DEBUG = true;
	}

	@Override
	protected CommandType perform(ZShop main) {
		main.getCommandManager().sendHelp("shop", sender);
		return CommandType.SUCCESS;
	}

}
