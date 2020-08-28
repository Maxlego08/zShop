package fr.maxlego08.shop.zshop.utils;

public enum EnumCategory {

	DEFAULT(0),
	SHOP(1),
	BUY(2),
	SELL(3),
	CONFIRM(4),
	CONFIG(5),

	;

	private final int inventoryID;

	private EnumCategory(int inventoryID) {
		this.inventoryID = inventoryID;
	}

	/**
	 * @return the inventoryID
	 */
	public int getInventoryID() {
		return inventoryID;
	}

}
