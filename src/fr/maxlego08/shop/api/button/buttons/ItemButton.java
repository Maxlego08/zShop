package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.IEconomy;
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
	 * Get max stack amount
	 * @return max stack item
	 */
	public int getMaxStack();
	
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
	 * 
	 * @return
	 */
	public IEconomy getIEconomy();
	
	/**
	 * Get item lore
	 * @return lore
	 */
	public List<String> getLore();
	
	/**
	 * 
	 * @return
	 */
	public ItemStack createItemStack();
	
	
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
