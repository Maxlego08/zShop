package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopDefault extends VCommand {

	public CommandShopDefault() {
		this.addSubCommand("default");
		this.DEBUG = true;
		this.setPermission(Permission.SHOP_DEFAULT);
	}
	
	@Override
	protected CommandType perform(ZShop main) {

		categories.saveDefault();
		inventories.saveDefault();
		
		message(sender, "§cDo now §6/shop reload§c for reload configuration !");
		
		return CommandType.SUCCESS;
	}

}
