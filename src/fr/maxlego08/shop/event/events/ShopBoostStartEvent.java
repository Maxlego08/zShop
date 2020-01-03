package fr.maxlego08.shop.event.events;

import fr.maxlego08.shop.event.ShopEvent;
import fr.maxlego08.shop.zshop.boost.BoostItem;

public class ShopBoostStartEvent extends ShopEvent {

	private BoostItem boostItem;

	public ShopBoostStartEvent(BoostItem boostItem) {
		super();
		this.boostItem = boostItem;
	}

	/**
	 * @return the boostItem
	 */
	public BoostItem getBoostItem() {
		return boostItem;
	}

	/**
	 * @param boostItem
	 *            the boostItem to set
	 */
	public void setBoostItem(BoostItem boostItem) {
		this.boostItem = boostItem;
	}

}
