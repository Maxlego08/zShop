package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopAll extends VCommand {

	public CommandShopAll() {
		this.addSubCommand("all");
		this.setDescription("Sells all items from your inventory");
		this.setConsoleCanUse(false);
		this.setPermission(Permission.SHOP_HAND_ALL);
	}

	@Override
	protected CommandType perform(ZShop main) {
		main.getShop().sellAll(player);
		return CommandType.SUCCESS;
	}
	
}
