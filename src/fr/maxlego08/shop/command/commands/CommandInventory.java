package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.commands.CommandType;

public class CommandInventory extends VCommand {

	private final Command command;

	/**
	 * @param command
	 */
	public CommandInventory(CommandManager commandManager, Command command) {
		super(commandManager);
		this.command = command;
		this.setPermission(command.getPermission());
		this.setDescription(command.getDescription());
		this.setConsoleCanUse(false);
	}

	@Override
	protected CommandType perform(ZShop plugin) {
		plugin.getShopManager().open(player, command);
		return CommandType.SUCCESS;
	}

}
