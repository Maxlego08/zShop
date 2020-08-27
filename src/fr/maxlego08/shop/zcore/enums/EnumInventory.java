package fr.maxlego08.shop.zcore.enums;

public enum EnumInventory {

	INVENTORY_DEFAULT(1),
	INVENTORY_SHOP(2),
	INVENTORY_CONFIRM(3),
	
	;
	
	private final int id;

	private EnumInventory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
