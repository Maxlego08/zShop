package fr.maxlego08.shop.button;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.enums.ButtonType;

public class ZButton implements Button {

	private final ButtonType type;
	private final ItemStack itemStack;
	private final int slot;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 */
	public ZButton(ButtonType type, ItemStack itemStack, int slot) {
		super();
		this.type = type;
		this.itemStack = itemStack;
		this.slot = slot;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public ButtonType getType() {
		return type;
	}

	@Override
	public int getSlot() {
		return slot;
	}

}
