package fr.maxlego08.shop.api.enums;

public enum InventoryType {

	DEFAULT, BUY, SELL, 
	
	CONFIRM,
	;

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static InventoryType form(String string) {
		if (string == null)
			return DEFAULT;
		try {
			InventoryType type = valueOf(string.toUpperCase());
			return type == null ? DEFAULT : type;
		} catch (Exception e) {
			return DEFAULT;
		}
	}

	public boolean isBuy() {
		return this == BUY;
	}

	public boolean isSell() {
		return this == SELL;
	}

	public boolean isConfirm() {
		return this == CONFIRM;
	}

	public boolean isDefault() {
		return this == DEFAULT;
	}

	public boolean isShop() {
		return (isBuy() || isSell());
	}

}
