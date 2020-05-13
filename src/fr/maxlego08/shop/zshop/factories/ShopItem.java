package fr.maxlego08.shop.zshop.factories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.zshop.items.Economy;

public interface ShopItem {

	/**
	 * 
	 * @return
	 */
	public ShopType getType();

	/**
	 * 
	 * @return
	 */
	public int getCategory();

	/**
	 * 
	 * @return
	 */
	public int getSlot();

	/**
	 * 
	 * @param player
	 * @param amount
	 */
	public void performBuy(Player player, int amount);

	/**
	 * 
	 * @param player
	 * @param amount
	 */
	public void performSell(Player player, int amount);

	/**
	 * 
	 * @return
	 */
	public ItemStack getItem();

	/**
	 * 
	 * @return
	 */
	public ItemStack getDisplayItem();

	/**
	 * 
	 * @return
	 */
	public double getSellPrice();

	/**
	 * 
	 * @return
	 */
	public double getBuyPrice();

	/**
	 * 
	 * @return
	 */
	public int getMaxStackSize();

	/**
	 * 
	 * @return
	 */
	public boolean useConfirm();

	/**
	 * 
	 * @return
	 */
	public Economy getEconomyType();

	/**
	 * 
	 * @param slot
	 */
	void setSlot(int slot);

	/**
	 * 
	 * @return
	 */
	boolean isSellable();

	/**
	 * 
	 * @return
	 */
	boolean isBuyable();

	/**
	 * 
	 * @return
	 */

	double getDefaultSellPrice();

	/**
	 * 
	 * @return
	 */
	double getDefaultBuyPrice();

	public enum ShopType {

		ITEM, ITEM_SLOT, UNIQUE_ITEM,

	}

}
