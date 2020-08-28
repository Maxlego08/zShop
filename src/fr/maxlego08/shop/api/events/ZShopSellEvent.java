package fr.maxlego08.shop.api.events;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.zcore.utils.event.CancelledShopEvent;

public class ZShopSellEvent extends CancelledShopEvent {

	private final ItemButton button;
	private final Player player;
	private int amount;
	private double price;
	private Economy economy;

	/**
	 * @param button
	 * @param player
	 * @param amount
	 * @param price
	 * @param economy
	 */
	public ZShopSellEvent(ItemButton button, Player player, int amount, double price, Economy economy) {
		super();
		this.button = button;
		this.player = player;
		this.amount = amount;
		this.price = price;
		this.economy = economy;
	}

	/**
	 * @return the button
	 */
	public ItemButton getButton() {
		return button;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the economy
	 */
	public Economy getEconomy() {
		return economy;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @param economy
	 *            the economy to set
	 */
	public void setEconomy(Economy economy) {
		this.economy = economy;
	}

}
