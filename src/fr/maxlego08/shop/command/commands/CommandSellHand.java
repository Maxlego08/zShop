package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.command.OptionalCommand;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.commands.CommandType;

public class CommandSellHand extends VCommand {

	public CommandSellHand(CommandManager commandManager, OptionalCommand optionalCommand) {
		super(commandManager);
		this.addSubCommand("all");
		this.setDescription(optionalCommand.getDescription());
		this.setPermission(optionalCommand.getPermission());
		this.setConsoleCanUse(false);
		this.addOptionalArg("amoun");
	}

	@Override
	protected CommandType perform(ZShop plugin) {
		plugin.getShopManager().sellHand(player, argAsInteger(0, 64));
		return CommandType.SUCCESS;
	}

}
