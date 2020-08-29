package fr.maxlego08.shop.api.button;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.enums.ButtonType;

public interface Button {

	/**
	 * 
	 * @return item
	 */
	public ItemStack getItemStack();

	/**
	 * 
	 * @return
	 */
	public ItemStack getCustomItemStack(Player player);

	/**
	 * 
	 * @return buttonType
	 */
	public ButtonType getType();

	/**
	 * 
	 * @return slot
	 */
	public int getSlot();

	/**
	 * Set tmp slot
	 * 
	 * @param slot
	 */
	public void setTmpSlot(int slot);

	/**
	 * 
	 * @return tmp slot
	 */
	public int getTmpSlot();

	/**
	 * 
	 * @return
	 */
	public boolean isClickable();

	/**
	 * 
	 * @param classz
	 * @return
	 */
	public <T extends Button> T toButton(Class<T> classz);

	/**
	 * 
	 * @return true is button is permanent
	 */
	public boolean isPermament();

}
