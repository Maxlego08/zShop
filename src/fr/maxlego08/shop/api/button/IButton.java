package fr.maxlego08.shop.api.button;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.enums.ButtonType;

public interface IButton {

	/**
	 * 
	 * @return item
	 */
	public ItemStack getItemStack();
	
	/**
	 * 
	 * @return buttonType
	 */
	public ButtonType getType();
	
	/**
	 * 
	 * @return else button
	 */
	public IButton getElseButton();
	
	/**
	 * 
	 * @return permission
	 */
	public String getPermission();
	
	/**
	 * 
	 * @return true if permission is not null
	 */
	public boolean hasPermission();
	
	/**
	 * 
	 * @return slot
	 */
	public int getSlot();
	
}
