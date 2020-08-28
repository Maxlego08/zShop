package fr.maxlego08.shop.command.commands;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.boost.BoostType;

public class CommandShopBoost extends VCommand {

	public CommandShopBoost() {
		this.addSubCommand("boost");
		this.addRequireArg("item id:item data");
		this.addRequireArg("boost type");
		this.addRequireArg("modifier");
		this.addRequireArg("time in second");
		this.setDescription("Add item price modifier");
		this.addSubCommand(new CommandShopBoostStop());
		this.addSubCommand(new CommandShopBoostShow());
		this.setPermission(Permission.SHOP_BOOST);
	}

	@Override
	protected CommandType perform(ZShop main) {
		
		String item = argAsString(0);
		BoostType type = BoostType.valueOf(argAsString(1).toUpperCase());
		double modifier = argAsDouble(2);
		long timeInSecond = argAsLong(3);
		
		MaterialData material;
		
		if (item.contains(":")) {
			String[] split = item.split(":");
			material = new MaterialData(getMaterial(Integer.valueOf(split[0])), Integer.valueOf(split[1]));
		}else
			material = new MaterialData(getMaterial(Integer.valueOf(item)));
		
		boost.boost(player, material, type, modifier, timeInSecond);
		
		return CommandType.SUCCESS;
	}

}
