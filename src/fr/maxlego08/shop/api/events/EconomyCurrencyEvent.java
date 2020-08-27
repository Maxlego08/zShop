package fr.maxlego08.shop.api.events;

import fr.maxlego08.shop.zcore.utils.event.ShopEvent;

public class EconomyCurrencyEvent extends ShopEvent {

	private String currency = "$";

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
