package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.command.OptionalCommand;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.commands.CommandType;

public class CommandSellAll extends VCommand {

	public CommandSellAll(CommandManager commandManager, OptionalCommand optionalCommand) {
		super(commandManager);
		this.addSubCommand("all");
		this.setDescription(optionalCommand.getDescription());
		this.setPermission(optionalCommand.getPermission());
		this.setConsoleCanUse(false);
	}

	@Override
	protected CommandType perform(ZShop plugin) {
		plugin.getShopManager().sellAll(player);
		return CommandType.SUCCESS;
	}

}
