package fr.maxlego08.shop.api.events;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.zcore.utils.event.ShopEvent;

public class EconomyMoneyEvent extends ShopEvent {

	private final Player player;
	private double money;

	public EconomyMoneyEvent(Player player) {
		super();
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

}
