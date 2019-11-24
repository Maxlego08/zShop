package fr.maxlego08.shop.save;

import java.util.ArrayList;
import java.util.List;

import fr.maxlego08.shop.zcore.utils.inventory.Button;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zcore.utils.storage.Saveable;

public class Lang implements Saveable {

	public static String prefix = "§8(§bzShop§8)";
	
	public static String commandHelp = "§a» §6%syntaxe% §7- §e%description%";
	public static String noPermission = "§cYou don't have permission !";
	public static String syntaxeError = "§cYou must execute the command like this §7: §a%command%";
	public static String commandError = "§cThis argument does not exist!";
	public static String onlinePlayerCanUse = "§cYou must be player to do this !";
	
	public static String shopInventoryItem = "§a%category% §e%page%§7/§6%maxPage%";
	public static String shopInventoryUniqueItem = "§a%category%";
	
	public static String canSell = "§cImpossible d'acheter";
	public static String canBuy = "§cImpossible de vendre";
	public static String sellItem = "§aVous venez de vendre §6%item%§a pour §6%price%§a$ !";
	public static String buyItem = "§aVous venez d'acheter §6%item%§a pour §6%price%§a$ !";
	public static String notEnouhtItems = "§cVous n'avez pas assez d'item dans votre inventaire.";
	public static String notEnouhtMoney = "§cVous n'avez pas assez d'argent.";
	public static String notEnouhtPlace = "§cVous avez l'inventaire plein !";
	public static String notItems = "§cVous n'avez pas cet item dans votre inventaire.";
	
	public static List<String> displayItemLore = new ArrayList<String>();
	
	public static String second = "second";
	public static String minute = "minute";
	public static String hour = "hour";
	public static String day = "day";
	
	public static Button backButton = new Button("§cRetour", 399, 0);
	public static Button nextButton = new Button("§eNext", 262, 0);
	public static Button previousButton = new Button("§cPrevious", 262, 0);
	public static Button buyButton = new Button("§aAchater", 160, 13);
	public static Button backBuyButton = new Button("§cRetour", 160, 14);
	public static Button resetItemButton = new Button("§cMettre à 1", 160, 14);
	public static Button removeTenItemButton = new Button("§cRetirer 10", 160, 1);
	public static Button removeOneItemButton = new Button("§cRetirer 1", 160, 4);
	public static Button addOneItemButton = new Button("§aAjouter 1", 160, 4);
	public static Button addTenItemButton = new Button("§aAjouter 10", 160, 5);
	public static Button maxItemButton = new Button("§aMettre à %max%", 160, 13);
	
	static{
		
		displayItemLore.add("§f» §2Prix d'achat§7: §a%buyPrice%");
		displayItemLore.add("§f» §2Prix de vente§7: §a%sellPrice%");
		displayItemLore.add("");
		displayItemLore.add("§f» §7Clique gauche pour acheter");
		displayItemLore.add("§f» §7Clique molette pour tout vendre");
		displayItemLore.add("§f» §7Clique droit pour vendre");
		
	}
	
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
