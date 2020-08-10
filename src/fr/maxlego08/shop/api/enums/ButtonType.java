package fr.maxlego08.shop.api.enums;

import fr.maxlego08.shop.api.exceptions.ButtonTypeException;

public enum ButtonType {

	NONE,

	BACK,

	NEXT,

	HOME,

	PREVIOUS,

	ITEM,

	ITEM_CONFIRM,

	INVENTORY,;

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

	/**
	 * 
	 * @return
	 */
	public boolean isPermament() {
		switch (this) {
		case INVENTORY:
		case ITEM:
		case ITEM_CONFIRM:
		case NONE:
		default:
			return false;
		case BACK:
		case HOME:
		case NEXT:
		case PREVIOUS:
			return true;
		}
	}

}
