package fr.maxlego08.zshop.api.event;

import org.bukkit.Bukkit;
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
	
	public void call(){
		Bukkit.getPluginManager().callEvent(this);
	}

}