package fr.maxlego08.shop.command.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.command.VCommand;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.save.inventory.ConfigDefaultInventory;
import fr.maxlego08.shop.zcore.utils.inventory.Button;
import fr.maxlego08.shop.zshop.categories.Category;
import fr.maxlego08.shop.zshop.items.ShopItem;
import fr.maxlego08.shop.zshop.items.ShopItemConsomable;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;

public class CommandShopDefauldConfig extends VCommand {

	@SuppressWarnings("deprecation")
	@Override
	public CommandType perform(ZShop main) {

		main.getCategories().getCategories().clear();
		main.getCategories().getCategories().put(1,
				new Category(1, 0, ShopType.ITEM, "§eBlocks", 2, null, 54, 49, 49, 50));
		main.getCategories().getCategories().put(2,
				new Category(2, 1, ShopType.ITEM, "§eFarm", 296, null, 54, 49, 49, 50));
		main.getCategories().getCategories().put(3,
				new Category(3, 2, ShopType.ITEM, "§eMobs", 367, null, 54, 49, 49, 50));
		main.getCategories().getCategories().put(4,
				new Category(4, 3, ShopType.ITEM, "§eFoods", 364, null, 54, 49, 49, 50));
		main.getCategories().getCategories().put(5,
				new Category(5, 4, ShopType.ITEM, "§eRedstones", 331, null, 54, 49, 49, 50));
		main.getCategories().getCategories().put(6,
				new Category(6, 5, ShopType.ITEM, "§eOres", 265, null, 54, 49, 49, 50));

		ConfigDefaultInventory.inventorySize = 9;
		ConfigDefaultInventory.decoration = new HashMap<Integer, Button>();

		List<ShopItem> items = new ArrayList<ShopItem>();
		items.add(new ShopItemConsomable(1, new ItemStack(Material.DIRT), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.STONE), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(17)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.GRASS), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.SAND), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.OBSIDIAN), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.QUARTZ_BLOCK), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.ICE), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.PACKED_ICE), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.TNT), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.COBBLESTONE), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.GRAVEL), 30, 2, 64));
		main.getItems().setItems(items, 1);

		items = new ArrayList<ShopItem>();
		items.add(new ShopItemConsomable(1, new ItemStack(Material.WHEAT), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(392)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(391)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.CAKE), 30, 2, 64));
		main.getItems().setItems(items, 2);

		items = new ArrayList<ShopItem>();
		items.add(new ShopItemConsomable(1, new ItemStack(Material.ENDER_PEARL), 30, 2, 16));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(367)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(375)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.BLAZE_ROD), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.STRING), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.SLIME_BALL), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.EGG), 30, 2, 16));
		main.getItems().setItems(items, 3);

		items = new ArrayList<ShopItem>();
		items.add(new ShopItemConsomable(1, new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 0), 30, 2, 16));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1), 30, 2, 16));
		for (int a = 0; a != 4; a++)
			items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(349), 1, (byte) a), 30, 2, 16));
		main.getItems().setItems(items, 4);
		
		items = new ArrayList<ShopItem>();
		items.add(new ShopItemConsomable(1, new ItemStack(Material.REDSTONE), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(29)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(33)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(356)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(404)), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(154)), 30, 2, 64));
		
		main.getItems().setItems(items, 5);
		items = new ArrayList<ShopItem>();
		items.add(new ShopItemConsomable(1, new ItemStack(Material.COAL), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.COAL, 1, (byte)1), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.IRON_INGOT), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.GOLD_INGOT), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.DIAMOND), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(getMaterial(351), 1, (byte)4), 30, 2, 64));
		items.add(new ShopItemConsomable(1, new ItemStack(Material.EMERALD), 30, 2, 64));
		main.getItems().setItems(items, 6);
		
		main.getItems().save();

		sendMessage(Lang.prefix + " §eCreate default config !");

		return CommandType.SUCCESS;
	}

}
