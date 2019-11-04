package fr.maxlego08.shop.save;

import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zcore.utils.storage.Saveable;

public class Lang implements Saveable {

	public static String prefix = "§8(§bLang§8)";
	
	public static String commandHelp = "§a» §6%syntaxe% §7- §e%description%";
	public static String noPermission = "§cYou don't have permission !";
	public static String syntaxeError = "§cYou must execute the command like this §7: §a%command%";
	public static String commandError = "§cThis argument does not exist!";
	public static String onlinePlayerCanUse = "§cYou must be player to do this !";
	
	public static String second = "second";
	public static String minute = "minute";
	public static String hour = "hour";
	public static String day = "day";
	
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
