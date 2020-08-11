package fr.maxlego08.shop.command.commands.zshop;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.enums.Permission;
import fr.maxlego08.shop.zcore.utils.commands.CommandType;

public class CommandZShopReload extends VCommand {

	public CommandZShopReload() {
		this.setPermission(Permission.ZSHOP_RELOAD.getPermission());
		this.addSubCommand("reload");
		this.addSubCommand("rl");
	}

	@Override
	protected CommandType perform(ZShop main) {
		
		main.getShopManager().reload();
		message(sender, "§eReload... §7Go to the console for more information.");
		
		return CommandType.SUCCESS;
	}

}
