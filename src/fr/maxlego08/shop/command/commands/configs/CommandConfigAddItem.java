package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandConfigAddItem extends VCommand {

	public CommandConfigAddItem() {
		this.addSubCommand("add");
		this.setPermission(Permission.SHOP_CONFIG_ADD_ITEM);
		this.addRequireArg("category id");
		this.addRequireArg("item id:item data");
		this.addRequireArg("sell price");
		this.addRequireArg("buy price");
		this.addRequireArg("max item stack size");
		this.setDescription("Add item in shop");
		this.setShowHelp(false);
		this.DEBUG = true;
	}

	@Override
	protected CommandType perform(ZShop main) {
		
		int id = argAsInteger(0);
		String item = argAsString(1);
		double sellPrice = argAsDouble(2);
		double buyPrice = argAsDouble(3);
		int maxStack = argAsInteger(4);
		
		MaterialData material;
		
		if (item.contains(":")) {
			String[] split = item.split(":");
			material = new MaterialData(getMaterial(Integer.valueOf(split[0])), Integer.valueOf(split[1]));
		}else
			material = new MaterialData(getMaterial(Integer.valueOf(item)));
		
		items.addItem(sender, categories.getCategory(id), sellPrice, buyPrice, material, maxStack);
		
		return CommandType.SUCCESS;
	}

}
