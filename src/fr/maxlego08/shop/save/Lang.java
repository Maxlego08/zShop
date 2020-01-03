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
	public static String sellItem = "§aVous venez de vendre §6x%amount% %item%§a pour §6%price%§a$ !";
	public static String buyItem = "§aVous venez d'acheter §6x%amount% %item%§a pour §6%price%§a$ !";
	public static String buyUniqueItem = "§aVous venez d'acheter §6%item%§a pour §6%price%§a$ !";
	public static String notEnouhtItems = "§cVous n'avez pas assez d'item dans votre inventaire.";
	public static String notEnouhtMoney = "§cVous n'avez pas assez d'argent.";
	public static String notEnouhtPlace = "§cVous avez l'inventaire plein !";
	public static String notItems = "§cVous n'avez pas cet item dans votre inventaire.";
	public static String and = "et";
	public static String sellHandAll = "§aVous venez de vendre §6%item% §apour §6%price%§a$.";
	public static String sellHandAllItem = "§ax§e%amount% §2%item%§a";
	public static String sellHandEmpty = "§cImpossible de trouver un item à vendre";
	public static String sellHandAir = "§cVous ne pouvez pas vendre de l'air !";
	public static String boostError = "§cImpossible de boost cet item, un boost est déjà en cours.";
	public static String boostSuccess = "§aVous venez de créer un boost pour l'item §6%item%§a.";
	public static String boostBroadcast = "§aL'item §2%item% §avient d'avoir un boost §2%boosttype% §apendant §2%timer% §avec un multiplicateur de §2%modifier%§a.";
	public static String boostBuy = "d'achat";
	public static String boostSell = "de vente";
	public static String boostItemSell = "§7§m%defaultPrice%$§b » §a§l%newPrice%";
	public static String boostItemBuy = "§7§m%defaultPrice%$§b » §a§l%newPrice%";
	public static String boostErrorFound = "§cAucun boost n'est disponible pour §6%item%§c.";
	public static String boostStopSuccess = "§aVous venez d'arreter le boost sur §6%item%§a.";
	public static String boostEmpty = "§cAucun boost en cours.";
	public static String boostShow = "§aBoost §2%type% §asur l'item §2%item% §apendant§6 %timer% §aavec un multiplicateur de§2 %modifier% §a.";
	
	public static String configAddItemError = "§cL'item §6%item% §cexiste déjà dans la shop !";
	public static String configAddItemErrorCategory = "§cLa categorie séléctionné ne peut pas avoir des items en vente !";
	public static String configAddItemSuccess = "§aVous venez d'ajouter l'item §6%item% §adans la categorie §6%category%§a.";
	public static String configRemoveItemSuccess = "§aVous venez de supprimer l'item §6%item% §ade la categorie §6%category%§a.";
	public static String configRemoveItemError = "§cL'item §6%item% §cn'existe pas dans la shop !";
	public static String configEditError = "§cImpossible de trouver l'item !";
	public static String configEditSuccess = "§aVous venez de modifier le prix §6%type% §apour l'item §6%item% §aà §6%price%§a$.";
	
	public static List<String> displayItemLore = new ArrayList<String>();
	public static List<String> displayItemLoreBoost = new ArrayList<String>();
	
	public static String second = "second";
	public static String minute = "minute";
	public static String hour = "hour";
	public static String day = "day";
	
	public static Button backButton = new Button("§cRetour", 399, 0);
	public static Button nextButton = new Button("§eNext", 262, 0);
	public static Button previousButton = new Button("§cPrevious", 262, 0);
	public static Button buyButton = new Button("§aAcheter", 160, 13);
	public static Button sellButton = new Button("§aVendre", 160, 13);
	public static Button backBuyButton = new Button("§cRetour", 160, 14);
	public static Button resetItemButton = new Button("§cMettre à 1", 160, 14);
	public static Button removeTenItemButton = new Button("§cRetirer 10", 160, 1);
	public static Button removeOneItemButton = new Button("§cRetirer 1", 160, 4);
	public static Button addOneItemButton = new Button("§aAjouter 1", 160, 4);
	public static Button addTenItemButton = new Button("§aAjouter 10", 160, 5);
	public static Button maxItemButton = new Button("§aMettre à %max%", 160, 13);
	public static Button confirmButton = new Button("§aConfirmer", 160, 13);
	
	static{
		
		displayItemLore.add("§f» §2Prix d'achat§7: §a%buyPrice%$");
		displayItemLore.add("§f» §2Prix de vente§7: §a%sellPrice%$");
		displayItemLore.add("%boostinfo%");
		displayItemLore.add("§f» §7Clique gauche pour acheter");
		displayItemLore.add("§f» §7Clique molette pour tout vendre");
		displayItemLore.add("§f» §7Clique droit pour vendre");
		
		displayItemLoreBoost.add("");
		displayItemLoreBoost.add("§f» §7Boost de §ax%modifier% §7!");
		displayItemLoreBoost.add("§f» §7Fin du boost dans §2%ending%");
		displayItemLoreBoost.add("");
		
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
