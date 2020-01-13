package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;

public class CommandShopCategory extends VCommand {

	@Override
	protected CommandType perform(ZShop main) {

		categories.saveDefault();
		inventories.saveDefault();
		
		message(sender, "§aDefault save create !");
		
		return CommandType.SUCCESS;
	}

}
