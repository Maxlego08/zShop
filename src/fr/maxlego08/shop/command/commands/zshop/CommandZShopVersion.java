package fr.maxlego08.shop.command.commands.zshop;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.enums.Permission;
import fr.maxlego08.shop.zcore.utils.commands.CommandType;

public class CommandZShopVersion extends VCommand {

	public CommandZShopVersion(CommandManager commandManager) {
		super(commandManager);
		this.setPermission(Permission.ZSHOP_VERSION.getPermission());
		this.addSubCommand("version");
		this.addSubCommand("v");
		this.addSubCommand("ver");
	}

	@Override
	protected CommandType perform(ZShop main) {
		
		sender.sendMessage("§7(§bzShop§7) §aVersion du plugin§7: §2" + main.getDescription().getVersion());
		sender.sendMessage("§7(§bzShop§7) §aAuteur§7: §2Maxlego08");
		sender.sendMessage("§7(§bzShop§7) §aDiscord§7: §2http://discord.groupez.xyz/");
		sender.sendMessage("§7(§bzShop§7) §aBuy it for §d8€§7: §2https://www.spigotmc.org/resources/74073/");
		String user = "%%__USER__%%";
		sender.sendMessage("§7(§bzShop§7) §aUser account§7: §2https://www.spigotmc.org/members/" + user);
		
		return CommandType.SUCCESS;
	}

}
