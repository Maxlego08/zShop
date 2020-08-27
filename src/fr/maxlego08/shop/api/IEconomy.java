package fr.maxlego08.shop.api;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.enums.Economy;

public interface IEconomy {

	/**
	 * 
	 * @param economy
	 * @param player
	 * @return
	 */
	double getMoney(Economy economy, Player player);
	
	/**
	 * 
	 * @param economy
	 * @param player
	 * @param price
	 * @return
	 */
	boolean hasMoney(Economy economy, Player player, double price);

	/**
	 * 
	 * @param economy
	 * @param player
	 * @param value
	 */
	void depositMoney(Economy economy, Player player, double value);
	
	/**
	 * 
	 * @param economy
	 * @param player
	 * @param value
	 */
	void withdrawMoney(Economy economy, Player player, double value);
}
