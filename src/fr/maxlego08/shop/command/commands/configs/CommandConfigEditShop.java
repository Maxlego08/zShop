package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandConfigEditShop extends VCommand {

	public CommandConfigEditShop() {
		this.setPermission(Permission.SHOP_CONFIG_SHOP);
		this.setConsoleCanUse(false);
		this.addSubCommand("gui");
		this.addRequireArg("category");
		this.addOptionalArg("page");
		this.setDescription("Edit shop GUI");
	}

	@Override
	protected CommandType perform(ZShop main) {
		String name = argAsString(0);
		int page = argAsInteger(1, 1);
		return this.shop.openConfigShop(player, name, page);
	}

}
