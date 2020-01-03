package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopBoostShow extends VCommand {

	public CommandShopBoostShow() {
		this.addSubCommand("show");
		this.setDescription("Show current boost");
		this.setPermission(Permission.SHOP_BOOST_SHOW);
	}
	
	@Override
	protected CommandType perform(ZShop main) {
		boost.show(sender);
		return CommandType.SUCCESS;
	}

}
