package fr.maxlego08.shop.zshop.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ShopItem{

	public ShopType getType();
	
	public int getCategory();
	
	public int getSlot();
	
	public void performBuy(Player player, int amount);
	
	public void performSell(Player player, int amount);
	
	public ItemStack getItem();
	
	public ItemStack getDisplayItem();
	
	public double getSellPrice();
	
	public double getBuyPrice();
	
	public int getMaxStackSize();
	
	public boolean useConfirm();
	
	public boolean isSellable();
	
	public boolean isBuyable();
	
	public Economy getEconomyType();
	
	public enum ShopType{
		
		ITEM,
		ITEM_SLOT,
		UNIQUE_ITEM,
		
	}
	
}
