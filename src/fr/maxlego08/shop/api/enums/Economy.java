package fr.maxlego08.shop.api.enums;

import org.bukkit.Bukkit;

import fr.maxlego08.shop.api.events.EconomyCurrencyEvent;
import fr.maxlego08.shop.save.Lang;

public enum Economy {

	VAULT,

	PLAYERPOINT,

	TOKENMANAGER,

	MYSQLTOKEN,

	CUSTOM,

	ICECORE,

	;

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static Economy get(String string) {
		if (string == null)
			return Economy.VAULT;
		for (Economy e : values())
			if (e.name().equalsIgnoreCase(string))
				return e;
		return Economy.VAULT;
	}

	public String getCurrenry() {
		switch (this) {
		case PLAYERPOINT:
			return Lang.currencyPlayerPoint;
		case VAULT:
			return Lang.currencyVault;
		case TOKENMANAGER:
			return Lang.currencyTokenManager;
		case MYSQLTOKEN:
			return Lang.currencyMySQLToken;
		case ICECORE:
			return Lang.currencyIceToken;
		case CUSTOM:
			EconomyCurrencyEvent event = new EconomyCurrencyEvent();
			Bukkit.getPluginManager().callEvent(event);
			return event.getCurrency();
		default:
			return "$";
		}
	}

}
