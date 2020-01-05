package fr.maxlego08.shop.zshop.items;

import fr.maxlego08.shop.save.Lang;

public enum Economy {

	VAULT,
	PLAYERPOINT,
	
	;

	public static Economy getOrDefault(String string, Economy eco) {
		for(Economy economy : values())
			if (string.equalsIgnoreCase(economy.name()))
				return economy;
		return eco;
	}
	
	public String toCurrency(){
		switch (this) {
		case PLAYERPOINT:
			return Lang.currencyPlayerPoint;
		case VAULT:
			return Lang.currencyVault;
		default:
			return "$";
		}
	}
	
}
