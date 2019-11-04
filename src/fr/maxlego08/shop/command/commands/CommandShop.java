package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class CommandShop extends VCommand {

	@Override
	public CommandType perform(ZShop main) {
		
		main.getShop().openShop(player, EnumCategory.DEFAULT, 0);
		
		return CommandType.SUCCESS;
	}

}
