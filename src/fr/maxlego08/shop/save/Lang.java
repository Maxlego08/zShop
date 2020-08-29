package fr.maxlego08.shop.save;

import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zcore.utils.storage.Saveable;

public class Lang implements Saveable {

	public static String prefix = "§8(§bzShop§8)";

	public static String commandHelp = "§a» §6%syntaxe% §7- §e%description%";
	public static String noPermission = "§cYou don't have permission !";
	public static String syntaxeError = "§cYou must execute the command like this §7: §a%command%";
	public static String commandError = "§cThis argument does not exist!";
	public static String commandCategoryError = "§cUnable to find categories with id §6%id%§c.";
	public static String commandItemError = "§cUnable to create the item with this id §6%id%§c.";
	public static String onlinePlayerCanUse = "§cYou must be player to do this !";

	public static String canSell = "§cUnable to sell";
	public static String canBuy = "§cUnable to buy";
	public static String sellItem = "§aYou just sold §6x%amount% %item%§a for §6%price%§a%currency% !";
	public static String buyItem = "§aYou have just bought §6x%amount% %item%§a for §6%price%§a%currency% !";
	public static String notEnouhtItems = "§cYou do not have enough items in your inventory.";
	public static String notEnouhtMoney = "§cYou do not have enough money.";
	public static String notEnouhtPlace = "§cYour inventory is ful";
	public static String notItems = "§cYou do not have this item in your inventory.";
	public static String and = "and";
	public static String sellAllError = "§cYou don't have any item to sell.";
	public static String sellHandAll = "§aYou just sold §6%item% §afor §6%price%§a%currency%.";
	public static String sellHandAllItem = "§ax§e%amount% §2%item%§a";
	public static String sellHandEmpty = "§cUnable to find an item for sale.";
	public static String sellHandAir = "§cYou can't sell air !";

	public static String buyPermission = "§d%percent%% §bof reduction";
	public static String sellPermission = "§d%percent%% §bincrease";
	public static String buyPermissionLine = "§bPrice reduced by §d%percent%§b%";
	public static String sellPermissionLine = "§bPrice increased by §d%percent%§b%";

	public static String second = "second";
	public static String minute = "minute";
	public static String hour = "hour";
	public static String day = "day";

	public static String currencyVault = "$";
	public static String currencyPlayerPoint = "£";
	public static String currencyTokenManager = "T";
	public static String currencyMySQLToken = "MT";
	public static String currencyIceToken = "*";

	private static transient Lang i = new Lang();

	@Override
	public void save(Persist persist) {
		persist.save(i, "lang");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, Lang.class, "lang");
	}

}
