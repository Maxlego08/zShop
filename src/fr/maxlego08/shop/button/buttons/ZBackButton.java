package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.BackButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.api.sound.SoundOption;

public class ZBackButton extends ZInventoryButton implements BackButton {

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param isPermanent
	 * @param action
	 * @param placeholder
	 * @param value
	 * @param inventory
	 * @param inventoryInterface
	 * @param plugin
	 */
	public ZBackButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, double value,
			String inventory, Inventory inventoryInterface, ZShop plugin, boolean glow, SoundOption sound) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value,
				inventory, inventoryInterface, plugin, glow, sound);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setBackInventory(Inventory inventory) {
		super.inventoryInterface = inventory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZBackButton [inventory=" + inventory + ", inventoryInterface=" + inventoryInterface + "]";
	}

}
