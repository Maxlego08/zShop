package fr.maxlego08.zshop.command.commands;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.command.VCommand;
import fr.maxlego08.zshop.zcore.enums.Message;
import fr.maxlego08.zshop.zcore.enums.Permission;
import fr.maxlego08.zshop.zcore.utils.commands.CommandType;

public class CommandShopReload extends VCommand {

	public CommandShopReload(ShopPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZSHOP_RELOAD);
		this.addSubCommand("reload", "rl");
		this.setDescription(Message.DESCRIPTION_RELOAD);
	}

	@Override
	protected CommandType perform(ShopPlugin plugin) {
		
		plugin.reloadFiles();
		message(plugin, sender, Message.RELOAD);
		
		return CommandType.SUCCESS;
	}

}
