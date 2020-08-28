package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.save.Lang;

public class CommandShopVersion extends VCommand {

	public CommandShopVersion() {
		this.addSubCommand("version", "v", "ver", "?");
		this.setDescription("Show plugin version");
	}
	
	@Override
	public CommandType perform(ZShop main) {

		sender.sendMessage(Lang.prefix + " §eVersion du plugin§7: §a" + main.getDescription().getVersion());
		sender.sendMessage(Lang.prefix + " §eAuteur§7: §aMaxlego08");
		sender.sendMessage(Lang.prefix + " §eDiscord§7: §ahttps://discord.gg/p9Mdste");
		String user = "%%__USER__%%";
		sender.sendMessage(Lang.prefix + " §eUser account§7: §ahttps://www.spigotmc.org/members/"+user);
		
		return CommandType.SUCCESS;
	}

}
