package fr.maxlego08.shop.save;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.maxlego08.shop.save.inventory.ConfigBuyInventory;
import fr.maxlego08.shop.save.inventory.ConfigInventoryConfirm;
import fr.maxlego08.shop.save.inventory.ConfigSellInventory;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zcore.utils.storage.Saveable;
import fr.maxlego08.shop.zshop.utils.Command;

public class Config implements Saveable {

	public static Map<String, Integer> citizens = new HashMap<String, Integer>();

	public static boolean shopOpenEvent = true;
	public static boolean shopPreSellEvent = true;
	public static boolean shopPostSellEvent = true;
	public static boolean shopPostBuyEvent = true;
	public static boolean shopPreBuyEvent = true;
	public static boolean shopPreSellAllEvent = true;
	public static boolean shopPostSellAllEvent = true;
	public static boolean shopBoostStartEvent = true;
	public static boolean shopBoostEndEvent = true;

	public static boolean logConsole = true;
	public static boolean broadcastMessageWhenBoostIsCreate = true;

	public static List<Command> commands = Arrays.asList(new Command("vip", Arrays.asList("getspawnereasly"), 2));

	public static ConfigBuyInventory buyInventory = new ConfigBuyInventory();
	public static ConfigSellInventory sellInventory = new ConfigSellInventory();
	public static ConfigInventoryConfirm confirmInventory = new ConfigInventoryConfirm();

	private static transient Config i = new Config();

	static {

		citizens.put("Michel", 1);
		citizens.put("Kevin", 2);
		citizens.put("§3C§dol§eor", 3);
		citizens.put("Shop", -1);

	}

	@Override
	public void save(Persist persist) {
		persist.save(i, "config");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, Config.class, "config");
	}

}
