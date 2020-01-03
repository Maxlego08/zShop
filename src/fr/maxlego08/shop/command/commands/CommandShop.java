package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class CommandShop extends VCommand {

	public CommandShop() {
		this.setPermission(Permission.SHOP_USE);
		this.addSubCommand("zshop");
		this.addSubCommand(new CommandShopVersion());
		this.addSubCommand(new CommandShopReload());
		this.addSubCommand(new CommandShopHelp());
		this.addSubCommand(new CommandShopHand());
		this.addSubCommand(new CommandShopAllHand());
		this.addSubCommand(new CommandShopAll());
		this.addSubCommand(new CommandShopBoost());
		this.setIgnoreParent(true);
		this.addOptionalArg("category");
		this.setDescription("Open shop inventory");
	}
	
	@Override
	public CommandType perform(ZShop main) {
		if (args.length != 0)
			return shop.openShop(player, argAsString(0));
		main.getShop().openShop(player, EnumCategory.DEFAULT, 0);
		return CommandType.SUCCESS;
	}

}
