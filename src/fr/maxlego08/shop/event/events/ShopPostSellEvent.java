package fr.maxlego08.shop.event.events;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.event.ShopEvent;
import fr.maxlego08.shop.zshop.items.ShopItem;

public class ShopPostSellEvent extends ShopEvent {

	private final ShopItem item;
	private final Player player;
	private final int quantity;
	private final double sellPrice;

	public ShopPostSellEvent(ShopItem item, Player player, int quantity, double sellPrice) {
		super();
		this.item = item;
		this.player = player;
		this.quantity = quantity;
		this.sellPrice = sellPrice;
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
	 * @return the sellPrice
	 */
	public double getSellPrice() {
		return sellPrice;
	}

}
