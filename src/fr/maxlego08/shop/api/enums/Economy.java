package fr.maxlego08.shop.api.enums;

import org.bukkit.Bukkit;

import fr.maxlego08.shop.api.events.EconomyCurrencyEvent;
import fr.maxlego08.shop.api.events.EconomyDenyEvent;
import fr.maxlego08.shop.zcore.enums.Message;

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
			return Message.CURRENCY_PLAYERPOINT.msg();
		case VAULT:
			return Message.CURRENCY_VAULT.msg();
		case TOKENMANAGER:
			return Message.CURRENCY_TOKENMANAGER.msg();
		case MYSQLTOKEN:
			return Message.CURRENCY_MYSQLTOKEN.msg();
		case LEVEL:
			return Message.CURRENCY_LEVEL.msg();
		case OPTECO:
			return Message.CURRENCY_OPTECO.msg();
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
			return Message.NOT_ENOUGH_MONEY_LEVEL.msg();
		case MYSQLTOKEN:
			return Message.NOT_ENOUGH_MONEY_MYSQLTOKEN.msg();
		case PLAYERPOINT:
			return Message.NOT_ENOUGH_MONEY_PLAYERPOINT.msg();
		case TOKENMANAGER:
			return Message.NOT_ENOUGH_MONEY_TOKENMANAGER.msg();
		case VAULT:
			return Message.NOT_ENOUGH_MONEY_VAULT.msg();
		case OPTECO:
			return Message.NOT_ENOUGH_MONEY_OPTECO.msg();
		default:
			return Message.NOT_ENOUGH_MONEY_VAULT.msg();
		}
	}

}
