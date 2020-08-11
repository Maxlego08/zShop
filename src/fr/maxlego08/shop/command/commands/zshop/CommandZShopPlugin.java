package fr.maxlego08.shop.command.commands.zshop;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.enums.Permission;
import fr.maxlego08.shop.zcore.utils.commands.CommandType;

public class CommandZShopPlugin extends VCommand {

	public CommandZShopPlugin() {

		this.setPermission(Permission.ZSHOP_USE.getPermission());
		this.addSubCommand(new CommandZShopReload());

	}

	@Override
	protected CommandType perform(ZShop main) {

		message(sender, "§ezShop");
		
		return CommandType.SUCCESS;
	}

}
