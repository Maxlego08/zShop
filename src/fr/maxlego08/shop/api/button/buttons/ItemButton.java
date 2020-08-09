package fr.maxlego08.shop.api.button.buttons;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.enums.Economy;

public interface ItemButton extends PermissibleButton {

	/**
	 * 
	 * @return sell price
	 */
	public double getSellPrice();
	
	/**
	 * 
	 * @return buy price
	 */
	public double getBuyPrice();
	
	/**
	 * 
	 * @return can sell
	 */
	public boolean canSell();
	
	/**
	 * 
	 * @return can buy
	 */
	public boolean canBuy();
	
	/**
	 * 
	 * @return economy
	 */
	public Economy getEconomy();
	
	/**
	 * 
	 * @return
	 */
	public boolean needToConfirm();
	
	/**
	 * Buy item
	 * @param player
	 * @param amount
	 */
	public void buy(Player player, int amount);
	
	/**
	 * Sell item
	 * @param player
	 * @param amount
	 */
	public void sell(Player player, int amount);
	
}