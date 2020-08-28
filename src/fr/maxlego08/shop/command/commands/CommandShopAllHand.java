package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopAllHand extends VCommand {

	public CommandShopAllHand() {
		this.addSubCommand("handall");
		this.setDescription("Sells all items inventory which are the same as the one being held in your hand ");
		this.setConsoleCanUse(false);
		this.setPermission(Permission.SHOP_HAND_ALL);
	}

	@Override
	protected CommandType perform(ZShop main) {
		shop.sellAllHand(player);
		return CommandType.SUCCESS;
	}
	
}
