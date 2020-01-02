package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopHand extends VCommand {

	public CommandShopHand() {
		this.addSubCommand("hand");
		this.addOptionalArg("amount");
		this.setDescription("Sell the item held in your hand");
		this.setConsoleCanUse(false);
		this.setPermission(Permission.SHOP_HAND);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected CommandType perform(ZShop main) {
		
		int amount = argAsInteger(0, player.getItemInHand().getAmount());
		
		if (amount > player.getItemInHand().getAmount())
			amount = player.getItemInHand().getAmount();
		
		main.getShop().sellHand(player, amount);
		
		return CommandType.SUCCESS;
	}

}
