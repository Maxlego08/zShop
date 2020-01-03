package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Message;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopConfig extends VCommand {

	public CommandShopConfig() {
		this.addSubCommand("config");
		this.setPermission(Permission.SHOP_CONFIG);
		this.addSubCommand(new CommandConfigCategoriesInformations());
		this.addSubCommand(new CommandConfigItemsInformations());
		this.addSubCommand(new CommandConfigAddItem());
		this.addSubCommand(new CommandConfigRemoveItem());
		this.addSubCommand(new CommandConfigEditItem());
		this.setDescription("Show help commande");
	}

	@Override
	protected CommandType perform(ZShop main) {

		message(sender, Message.COMMAND_CONFIG);
		subVCommands.forEach(command -> message(sender, Message.COMMAND_SYNTAXE_HELP, command.getSyntaxe(),
				command.getDescription()));
		return CommandType.SUCCESS;
	}

}
