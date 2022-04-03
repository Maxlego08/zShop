package fr.maxlego08.shop.api.events;

import fr.maxlego08.shop.zcore.utils.event.ShopEvent;

public class EconomyDenyEvent extends ShopEvent {

	private String message;

	public EconomyDenyEvent() {
		super();
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
