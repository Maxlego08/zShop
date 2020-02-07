package fr.maxlego08.shop.zshop.items;

import org.bukkit.Bukkit;

import fr.maxlego08.shop.event.events.economy.EconomyCurrencyEvent;
import fr.maxlego08.shop.save.Lang;

public enum Economy {

	VAULT, PLAYERPOINT, TOKENMANAGER, MYSQLTOKEN, CUSTOM,

	;

	public static Economy getOrDefault(String string, Economy eco) {
		for (Economy economy : values())
			if (string.equalsIgnoreCase(economy.name()))
				return economy;
		return eco;
	}

	public String toCurrency() {
		switch (this) {
		case PLAYERPOINT:
			return Lang.currencyPlayerPoint;
		case VAULT:
			return Lang.currencyVault;
		case TOKENMANAGER:
			return Lang.currencyTokenManager;
		case MYSQLTOKEN:
			return Lang.currencyMySQLToken;
		case CUSTOM:
			EconomyCurrencyEvent event = new EconomyCurrencyEvent();
			Bukkit.getPluginManager().callEvent(event);
			return event.getCurrency();
		default:
			return "$";
		}
	}

}
