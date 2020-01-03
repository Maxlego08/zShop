package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.enums.Permission;

public class CommandConfigRemoveItem extends VCommand {

	public CommandConfigRemoveItem() {
		this.addSubCommand("remove");
		this.setPermission(Permission.SHOP_CONFIG_DELETE_ITEM);
		this.addRequireArg("category id");
		this.addRequireArg("item id:item data");
		this.setDescription("Remove item in shop");
		this.setShowHelp(false);
		this.DEBUG = true;
	}

	@Override
	protected CommandType perform(ZShop main) {
		
		int id = argAsInteger(0);
		String item = argAsString(1);
		MaterialData material;
		
		if (item.contains(":")) {
			String[] split = item.split(":");
			material = new MaterialData(getMaterial(Integer.valueOf(split[0])), Integer.valueOf(split[1]));
		}else
			material = new MaterialData(getMaterial(Integer.valueOf(item)));
		
		items.removeItem(sender, categories.getCategory(id), material);
		
		return CommandType.SUCCESS;
	}

}
