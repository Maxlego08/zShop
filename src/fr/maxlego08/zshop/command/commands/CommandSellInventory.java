package fr.maxlego08.zshop.command.commands;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.command.VCommand;
import fr.maxlego08.zshop.zcore.utils.commands.CommandType;

public class CommandSellInventory extends VCommand {

	public CommandSellInventory(ShopPlugin plugin) {
		super(plugin);
		this.onlyPlayers();
	}

	@Override
	protected CommandType perform(ShopPlugin plugin) {

		plugin.getShopManager().openSellInventory(player);

		return CommandType.SUCCESS;
	}

}
