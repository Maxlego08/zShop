package fr.maxlego08.shop.api.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.zcore.utils.event.CancelledShopEvent;

public class ZShopButtonRenderEvent extends CancelledShopEvent {

	private final Button button;
	private final Player player;
	private int slot;
	private ItemStack itemStack;

	/**
	 * @param button
	 * @param player
	 * @param slot
	 * @param itemStack
	 */
	public ZShopButtonRenderEvent(Button button, Player player, int slot, ItemStack itemStack) {
		super();
		this.button = button;
		this.player = player;
		this.slot = slot;
		this.itemStack = itemStack;
	}

	/**
	 * @return the button
	 */
	public Button getButton() {
		return button;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the itemStack
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @param itemStack
	 *            the itemStack to set
	 */
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}

}
