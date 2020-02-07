package fr.maxlego08.shop.event.events.economy;

import fr.maxlego08.shop.event.EconomyEvent;

public class EconomyCurrencyEvent extends EconomyEvent {

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
