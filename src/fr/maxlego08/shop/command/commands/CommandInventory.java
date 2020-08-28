package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.command.OptionalAction;
import fr.maxlego08.shop.api.command.OptionalCommand;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.logger.Logger;
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
		this.DEBUG = true;

		OptionalCommand optionalCommand = command.getOptionalCommand(OptionalAction.SELL_ALL);
		if (optionalCommand.isPresent()) {
			this.addSubCommand(new CommandSellAll(commandManager, optionalCommand));
			Logger.info("Loading the sell all for command " + command.getCommand());
		}

		optionalCommand = command.getOptionalCommand(OptionalAction.SELL_HAND);
		if (optionalCommand.isPresent()) {
			this.addSubCommand(new CommandSellHand(commandManager, optionalCommand));
			Logger.info("Loading the sell hand for command " + command.getCommand());
		}

		optionalCommand = command.getOptionalCommand(OptionalAction.SELL_HAND_ALL);
		if (optionalCommand.isPresent()) {
			this.addSubCommand(new CommandSellHandAll(commandManager, optionalCommand));
			Logger.info("Loading the sell hand all for command " + command.getCommand());
		}

	}

	@Override
	protected CommandType perform(ZShop plugin) {
		plugin.getShopManager().open(player, command);

		message(player, "§cCeci est une version de développement et non de production, à utiliser pour des tests.");

		return CommandType.SUCCESS;
	}

}
