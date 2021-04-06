package fr.maxlego08.shop.button.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.PlaceholderButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.sound.SoundOption;

public class ZPlaceholderButton extends ZPermissibleButton implements PlaceholderButton {

	private final PlaceholderAction action;
	private final String placeholder;
	private final String value;

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
	 */
	public ZPlaceholderButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, String value,
			boolean needGlow, SoundOption sound) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, needGlow, sound);
		this.action = action;
		this.placeholder = placeholder;
		this.value = value;
	}

	@Override
	public String getPlaceHolder() {
		return placeholder;
	}

	@Override
	public PlaceholderAction getAction() {
		return action;
	}

	@Override
	public boolean hasPlaceHolder() {
		return placeholder != null && action != null;
	}

	@Override
	public boolean hasPermission() {
		return super.hasPermission() || this.hasPlaceHolder();
	}

	@Override
	public boolean checkPermission(Player player) {

		if (!this.hasPlaceHolder())

			return super.checkPermission(player);

		else {

			String valueAsString = papi(getPlaceHolder(), player);
			
			if (this.action.isString()) {
				switch (action) {
				case EQUALS_STRING:
					return valueAsString.equals(String.valueOf(this.value));
				case EQUALSIGNORECASE_STRING:
					return valueAsString.equalsIgnoreCase(String.valueOf(this.value));
				default:
					return super.checkPermission(player);
				}
			} else
				try {

					double value = Double.valueOf(valueAsString);
					double currentValue = Double.valueOf(this.value);

					switch (action) {
					case LOWER:
						return value < currentValue;
					case LOWER_OR_EQUAL:
						return value <= currentValue;
					case SUPERIOR:
						return value > currentValue;
					case SUPERIOR_OR_EQUAL:
						return value >= currentValue;
					case EQUALS_STRING:
						return valueAsString.equals(String.valueOf(this.value));
					case EQUALSIGNORECASE_STRING:
						return valueAsString.equalsIgnoreCase(String.valueOf(this.value));
					default:
						return super.checkPermission(player);
					}

				} catch (Exception e) {
					return super.checkPermission(player);
				}
		}
	}

	@Override
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZPlaceholderButton [action=" + action + ", placeholder=" + placeholder + ", value=" + value
				+ ", getElseButton()=" + getElseButton() + ", getPermission()=" + getPermission() + ", hasElseButton()="
				+ hasElseButton() + ", getMessage()=" + getMessage() + ", hasMessage()=" + hasMessage()
				+ ", getItemStack()=" + getItemStack() + ", getType()=" + getType() + ", getSlot()=" + getSlot()
				+ ", getTmpSlot()=" + getTmpSlot() + ", isClickable()=" + isClickable() + ", isPermament()="
				+ isPermament() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}

}
