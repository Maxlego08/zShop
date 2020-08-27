package fr.maxlego08.shop.button.buttons;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.SlotButton;
import fr.maxlego08.shop.api.enums.ButtonType;

public class ZButtonSlot extends ZPermissibleButton implements SlotButton {

	private final List<Integer> slots;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param isPermanent
	 * @param slots
	 */
	public ZButtonSlot(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, List<Integer> slots) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent);
		this.slots = slots;
	}

	@Override
	public List<Integer> getSlots() {
		return slots;
	}

}
