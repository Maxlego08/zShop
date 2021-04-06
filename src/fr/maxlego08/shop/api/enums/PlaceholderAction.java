package fr.maxlego08.shop.api.enums;

public enum PlaceholderAction {

	EQUALS_STRING,
	EQUALSIGNORECASE_STRING,
	
	SUPERIOR, LOWER,

	SUPERIOR_OR_EQUAL, LOWER_OR_EQUAL,;

	public static PlaceholderAction from(String string) {
		if (string == null)
			return null;
		for (PlaceholderAction action : values())
			if (action.name().equalsIgnoreCase(string))
				return action;
		return null;
	}
	
	public boolean isString() {
		return this == EQUALS_STRING || this == EQUALSIGNORECASE_STRING;
	}

}
