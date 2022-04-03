package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.command.OptionalCommand;
import fr.maxlego08.shop.api.enums.OptionalAction;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.enums.Message;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.utils.commands.CommandType;

public class CommandInventory extends VCommand {

	private final Command command;
	private OptionalCommand categoryCommand;

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

		categoryCommand = command.getOptionalCommand(OptionalAction.CATEGORY);
		if (categoryCommand.isPresent()) {
			this.setIgnoreArgs(true);
			this.setIgnoreParent(true);
			Logger.info("Loading the category for command " + command.getCommand());
		}

	}

	@Override
	protected CommandType perform(ZShop plugin) {

		if (this.args.length > 1) {
			return CommandType.SYNTAX_ERROR;
		}

		if (this.args.length == 0) {
			plugin.getShopManager().open(this.player, this.command);
		} else {

			if (!this.categoryCommand.hasPermission(this.sender)) {
				message(this.sender, Message.COMMAND_NO_PERMISSION);
				return CommandType.SUCCESS;
			}

			String category = argAsString(0);
			plugin.getShopManager().open(this.player, this.command, category);

		}
		return CommandType.SUCCESS;
	}

}
