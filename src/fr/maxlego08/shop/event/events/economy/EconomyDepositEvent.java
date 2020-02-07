package fr.maxlego08.shop.event.events.economy;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.event.EconomyEvent;

public class EconomyDepositEvent extends EconomyEvent {

	private final Player player;
	private final double money;

	public EconomyDepositEvent(Player player, double money) {
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
