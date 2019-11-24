package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.save.Lang;

public class CommandShopReload extends VCommand {

	@Override
	public CommandType perform(ZShop main) {

		main.getSavers().forEach(saver -> saver.load(main.getPersist()));
		main.getItems().load();
		
		sendMessage(Lang.prefix + " §aReload !");
		
		return CommandType.SUCCESS;
	}

}
