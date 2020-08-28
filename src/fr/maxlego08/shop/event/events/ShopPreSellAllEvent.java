package fr.maxlego08.shop.event.events;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.event.ShopEvent;

public class ShopPreSellAllEvent extends ShopEvent {

	private final Player player;
	private Map<ItemStack, Integer> items;
	private double price;

	public ShopPreSellAllEvent(Player player, Map<ItemStack, Integer> items, double price) {
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

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(Map<ItemStack, Integer> items) {
		this.items = items;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

}
