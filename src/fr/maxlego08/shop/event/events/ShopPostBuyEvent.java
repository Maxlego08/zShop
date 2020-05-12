package fr.maxlego08.shop.event.events;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.event.ShopEvent;
import fr.maxlego08.shop.zshop.factories.ShopItem;

public class ShopPostBuyEvent extends ShopEvent {

	private final ShopItem item;
	private final Player player;
	private final int quantity;
	private final double price;

	public ShopPostBuyEvent(ShopItem item, Player player, int quantity, double price) {
		super();
		this.item = item;
		this.player = player;
		this.quantity = quantity;
		this.price = price;
	}

	/**
	 * @return the item
	 */
	public ShopItem getItem() {
		return item;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

}
