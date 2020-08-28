package fr.maxlego08.shop.command.commands;

import java.util.List;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;

public class CommandShopCustom extends VCommand {

	public CommandShopCustom(List<String> aliases) {
		this.setConsoleCanUse(false);
		aliases.forEach(c -> addSubCommand(c));
	}

	@Override
	protected CommandType perform(ZShop main) {

		this.shop.openShop(player, command);

		return CommandType.SUCCESS;
	}

}
