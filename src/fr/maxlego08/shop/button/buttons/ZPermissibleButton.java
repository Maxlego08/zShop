package fr.maxlego08.shop.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.PermissibleButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.button.ZButton;

public class ZPermissibleButton extends ZButton implements PermissibleButton {

	private final String permission;
	private final String message;
	private final Button elseButton;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param elseButton
	 */
	public ZPermissibleButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton) {
		super(type, itemStack, slot);
		this.permission = permission;
		this.elseButton = elseButton;
		this.message = color(message);
	}

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param elseButton
	 */
	public ZPermissibleButton(ButtonType type, ItemStack itemStack, int slot) {
		super(type, itemStack, slot);
		this.permission = null;
		this.elseButton = null;
		this.message = null;
	}

	@Override
	public Button getElseButton() {
		return elseButton;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public boolean hasPermission() {
		return permission != null;
	}

	@Override
	public boolean hasElseButton() {
		return elseButton != null;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public boolean hasMessage() {
		return message != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZPermissibleButton [permission=" + permission + ", message=" + message + ", elseButton=" + elseButton
				+ "] => " + super.toString();
	}

}
