package fr.maxlego08.shop.command.commands.configs;

import java.util.List;

import org.bukkit.command.CommandSender;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandConfigEditShop extends VCommand {

	public CommandConfigEditShop() {
		this.setPermission(Permission.SHOP_CONFIG_SHOP);
		this.setConsoleCanUse(false);
		this.addSubCommand("gui");
		this.addRequireArg("category");
		this.addOptionalArg("page");
		this.setDescription("Edit shop GUI");
		this.setTabCompletor();
	}

	@Override
	protected CommandType perform(ZShop main) {
		String name = argAsString(0);
		int page = argAsInteger(1, 1);
		return this.shop.openConfigShop(player, name, page);
	}

	@Override
	public List<String> toTab(ZShop plugin, CommandSender sender2, String[] args) {
		return super.generateListContains(plugin.getCategories().toTabCompleter(), args[2]);
	}

}
