package fr.maxlego08.shop.command.commands.configs;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.categories.Category;

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

		MaterialData material = null;

		try {
			if (item.contains(":")) {
				String[] split = item.split(":");
				material = new MaterialData(getMaterial(Integer.valueOf(split[0])), Integer.valueOf(split[1]));
			} else
				material = new MaterialData(getMaterial(Integer.valueOf(item)));
		} catch (Exception e) {
			message(sender, Lang.commandItemError.replace("%id%", item));
			return CommandType.DEFAULT;
		}
		
		Category category = categories.getCategory(id);

		if (category == null) {
			message(sender, Lang.commandCategoryError.replace("%id%", String.valueOf(id)));
			return CommandType.DEFAULT;
		}
		
		items.removeItem(sender, category, material);
		
		return CommandType.SUCCESS;
	}

}
