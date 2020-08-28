package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandShopBoostStop extends VCommand {

	public CommandShopBoostStop() {
		this.addSubCommand("stop");
		this.addRequireArg("item id:item data");
		this.setDescription("Stop boost");
		this.setPermission(Permission.SHOP_BOOST_STOP);
		this.DEBUG = true;
	}

	@Override
	protected CommandType perform(ZShop main) {


		String item = argAsString(0);

		MaterialData material;

		if (item.contains(":")) {
			String[] split = item.split(":");
			material = new MaterialData(getMaterial(Integer.valueOf(split[0])), Integer.valueOf(split[1]));
		} else
			material = new MaterialData(getMaterial(Integer.valueOf(item)));

		boost.stopBoost(sender, material);
		return CommandType.SUCCESS;
	}

}
