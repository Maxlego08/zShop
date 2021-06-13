package fr.maxlego08.shop.api.enums;

import org.bukkit.Bukkit;

import fr.maxlego08.shop.api.events.EconomyCurrencyEvent;
import fr.maxlego08.shop.api.events.EconomyDenyEvent;
import fr.maxlego08.shop.save.Lang;

public enum Economy {

	VAULT,

	PLAYERPOINT,

	TOKENMANAGER,

	MYSQLTOKEN,

	CUSTOM,

	LEVEL,

	OPTECO,

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
		case LEVEL:
			return Lang.currencyLevel;
		case OPTECO:
			return Lang.currencyOptEco;
		case CUSTOM:
			EconomyCurrencyEvent event = new EconomyCurrencyEvent();
			Bukkit.getPluginManager().callEvent(event);
			return event.getCurrency();
		default:
			return "$";
		}
	}

	public String getDenyMessage() {
		switch (this) {
		case CUSTOM:
			EconomyDenyEvent event = new EconomyDenyEvent();
			Bukkit.getPluginManager().callEvent(event);
			return event.getMessage();
		case LEVEL:
			return Lang.notEnouhtMoneyLevel;
		case MYSQLTOKEN:
			return Lang.notEnouhtMoneyMySQLToken;
		case PLAYERPOINT:
			return Lang.notEnouhtMoneyPlayerPoint;
		case TOKENMANAGER:
			return Lang.notEnouhtMoneyTokenManager;
		case VAULT:
			return Lang.notEnouhtMoneyVault;
		case OPTECO:
			return Lang.notEnouhtMoneyOptEco;
		default:
			return Lang.notEnouhtMoneyVault;
		}
	}

}
