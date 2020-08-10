package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.HomeButton;
import fr.maxlego08.shop.api.enums.ButtonType;

public class ZHomeButton extends ZBackButton implements HomeButton {

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param inventory
	 */
	public ZHomeButton(ButtonType type, ItemStack itemStack, int slot, String inventory) {
		super(type, itemStack, slot, inventory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param inventory
	 */
	public ZHomeButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, String inventory) {
		super(type, itemStack, slot, permission, message, elseButton, inventory);
		// TODO Auto-generated constructor stub
	}

}
