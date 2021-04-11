package fr.maxlego08.shop.api.enums;

import fr.maxlego08.shop.api.exceptions.ButtonTypeException;

public enum ButtonType {

	NONE,

	NONE_SLOT,

	BACK,

	NEXT,

	HOME,

	PREVIOUS,

	ITEM,

	ITEM_CONFIRM,
	
	ITEM_CONFIRM_DOUBLE,

	INVENTORY,

	BUY_CONFIRM,

	SELL_CONFIRM,

	ADD,

	REMOVE,

	SET_TO_ONE,

	SET_TO_MAX,

	SHOW_ITEM,

	PERFORM_COMMAND,

	ZSPAWNER,

	;

	/**
	 * 
	 * @param string
	 * @param inventoryName
	 * @return
	 * @throws ButtonTypeException
	 */
	public static ButtonType from(String string, String inventoryName, String real) throws ButtonTypeException {
		if (string != null)
			for (ButtonType type : values())
				if (type.name().equalsIgnoreCase(string))
					return type;
		throw new ButtonTypeException("Impossible de trouver le " + string + " pour type dans l'inventaire "
				+ inventoryName + " (" + real + ")");
	}

	public boolean isClickable() {
		return this != NONE;
	}

	public boolean isShow() {
		return this == SHOW_ITEM;
	}

	public boolean isSlots() {
		return this == NONE_SLOT;
	}

	public boolean isOtherClick() {
		switch (this) {
		case ITEM:
		case ZSPAWNER:
			return true;
		case ADD:
		case BACK:
		case BUY_CONFIRM:
		case HOME:
		case INVENTORY:
		case ITEM_CONFIRM:
		case NEXT:
		case NONE:
		case NONE_SLOT:
		case PERFORM_COMMAND:
		case PREVIOUS:
		case REMOVE:
		case SELL_CONFIRM:
		case SET_TO_MAX:
		case SET_TO_ONE:
		case SHOW_ITEM:
		default:
			return false;
		}
	}

	public boolean isMove() {
		return NEXT == this || this == PREVIOUS;
	}

}
