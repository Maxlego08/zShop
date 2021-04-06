package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.NextButton;
import fr.maxlego08.shop.api.button.buttons.PreviousButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.sound.SoundOption;

public class ZMoveButton extends ZPlaceholderButton implements NextButton, PreviousButton {

	private final boolean display;

	public ZMoveButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, String value,
			boolean needGlow, SoundOption sound, boolean display) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value, needGlow,
				sound);
		this.display = display;
	}

	@Override
	public boolean isDisplay() {
		return display;
	}

}
