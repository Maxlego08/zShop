package fr.maxlego08.shop.zcore.utils.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopEvent extends Event {

	private final static HandlerList handlers = new HandlerList();

	/**
	 * @return the handlers
	 */
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
