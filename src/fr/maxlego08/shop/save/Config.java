package fr.maxlego08.shop.save;

import fr.maxlego08.shop.save.inventory.ConfigBuyInventory;
import fr.maxlego08.shop.save.inventory.ConfigDefaultInventory;
import fr.maxlego08.shop.save.inventory.ConfigInventoryConfirm;
import fr.maxlego08.shop.save.inventory.ConfigSellInventory;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zcore.utils.storage.Saveable;

public class Config implements Saveable {

	public static String usePermission = "zshop.use";
	public static String helpPermission = "zshop.help";
	
	public static boolean shopOpenEvent = true;
	public static boolean shopPreSellEvent = true;
	public static boolean shopPostSellEvent = true;
	public static boolean shopPostBuyEvent = true;
	public static boolean shopPreBuyEvent = true;
	
	public static boolean logConsole = true;
	
	public static ConfigDefaultInventory defaultInventory = new ConfigDefaultInventory();
	public static ConfigBuyInventory buyInventory = new ConfigBuyInventory();
	public static ConfigSellInventory sellInventory = new ConfigSellInventory();
	public static ConfigInventoryConfirm confirmInventory = new ConfigInventoryConfirm();
	
	private static transient Config i = new Config();
	
	@Override
	public void save(Persist persist) {
		persist.save(i, "config");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, Config.class, "config");
	}

}
