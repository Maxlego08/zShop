package fr.maxlego08.shop.command.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.zshop.items.ShopItem;
import fr.maxlego08.shop.zshop.items.ShopItemConsomable;

public class CommandShopTest extends VCommand {

	@Override
	public CommandType perform(ZShop main) {

		List<ShopItem> item = main.getItems().getItems(1);

		for (int a = 0; a != 340; a++) {
			item.add(new ShopItemConsomable(1, new ItemStack(Material.DIAMOND_BLOCK), 0.1, 10, 64, true, false,
					false, new ArrayList<String>()));
		}
		
		
		sendMessage("§2Voila !");
		
		return CommandType.SUCCESS;
	}

}
