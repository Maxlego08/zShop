package fr.maxlego08.shop.zcore.enums;

public enum Inventory {

	INVENTORY_DEFAULT(1),
	
	;
	
	private final int id;

	private Inventory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
