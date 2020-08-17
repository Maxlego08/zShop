package fr.maxlego08.shop.api.events;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.zcore.utils.event.ShopEvent;

public class EconomyWithdrawMoney extends ShopEvent {

	private final Player player;
	private final double money;

	public EconomyWithdrawMoney(Player player, double money) {
		super();
		this.player = player;
		this.money = money;
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
	
}
