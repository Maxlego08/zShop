package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopReload extends VCommand {

	public CommandShopReload() {
		this.addSubCommand("reload", "rl");
		this.setDescription("Reload configuration");
		this.setPermission(Permission.SHOP_RELOAD);
	}
	
	@Override
	public CommandType perform(ZShop main) {

		main.getSavers().forEach(saver -> saver.load(main.getPersist()));
		items.load();
		categories.load();
		inventories.load();
		
		main.getInventoryManager().updateAllPlayer();
		
		message(sender, "§aReload !");
		
		return CommandType.SUCCESS;
	}

}
