package fr.maxlego08.shop.event.events;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.event.ShopEvent;

public class ShopPostSellAllEvent extends ShopEvent {

	private final Player player;
	private final Map<ItemStack, Integer> items;
	private final double price;

	public ShopPostSellAllEvent(Player player, Map<ItemStack, Integer> items, double price) {
		super();
		this.player = player;
		this.items = items;
		this.price = price;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the items
	 */
	public Map<ItemStack, Integer> getItems() {
		return items;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	
}
