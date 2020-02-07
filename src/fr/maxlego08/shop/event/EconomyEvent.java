package fr.maxlego08.shop.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EconomyEvent extends Event{

	private final static HandlerList handlers = new HandlerList();

	/**
	 * @return the handlers
	 */
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
