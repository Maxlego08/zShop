package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.buttons.AddRemoveButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.button.ZButton;

public class ZAddRemoveButton extends ZButton implements AddRemoveButton {

	private final int amount;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param amount
	 */
	public ZAddRemoveButton(ButtonType type, ItemStack itemStack, int slot, int amount) {
		super(type, itemStack, slot);
		this.amount = amount;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

}
