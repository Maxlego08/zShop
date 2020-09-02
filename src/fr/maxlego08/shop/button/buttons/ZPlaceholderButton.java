package fr.maxlego08.shop.button.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.PlaceholderButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.zcore.logger.Logger;

public class ZPlaceholderButton extends ZPermissibleButton implements PlaceholderButton {

	private final PlaceholderAction action;
	private final String placeholder;
	private final double value;

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
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, double value) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent);
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

			try {
				double value = Double.valueOf(valueAsString);

				switch (action) {
				case LOWER:
					System.out.println("Lower " + value + " < " + this.value);
					return value < this.value;
				case LOWER_OR_EQUAL:
					System.out.println("LOWER_OR_EQUAL " + value + " <= " + this.value);
					return value <= this.value;
				case SUPERIOR:
					System.out.println("SUPERIOR " + value + " > " + this.value);
					return value > this.value;
				case SUPERIOR_OR_EQUAL:
					System.out.println("Superior or equal " + value + " >= " + this.value);
					return value >= this.value;
				default:
					return super.checkPermission(player);
				}

			} catch (Exception e) {
				Logger.info("Impossible de transformer la valeur " + valueAsString + " en double pour le placeholder "
						+ this.placeholder);
				return super.checkPermission(player);
			}

		}
	}

	@Override
	public double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZPlaceholderButton [action=" + action + ", placeholder=" + placeholder + ", value=" + value
				+ ", getElseButton()=" + getElseButton() + ", getPermission()=" + getPermission() + ", hasElseButton()="
				+ hasElseButton() + ", getMessage()=" + getMessage() + ", hasMessage()=" + hasMessage()
				+ ", getItemStack()=" + getItemStack() + ", getType()=" + getType()
				+ ", getSlot()=" + getSlot() + ", getTmpSlot()=" + getTmpSlot() + ", isClickable()=" + isClickable()
				+ ", isPermament()=" + isPermament() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}

	

}
