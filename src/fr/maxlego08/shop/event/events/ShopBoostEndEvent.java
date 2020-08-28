package fr.maxlego08.shop.event.events;

import fr.maxlego08.shop.event.ShopEvent;
import fr.maxlego08.shop.zshop.boost.BoostItem;

public class ShopBoostEndEvent extends ShopEvent {

	private final BoostItem boostItem;

	public ShopBoostEndEvent(BoostItem boostItem) {
		super();
		this.boostItem = boostItem;
	}

	/**
	 * @return the boostItem
	 */
	public BoostItem getBoostItem() {
		return boostItem;
	}

}
