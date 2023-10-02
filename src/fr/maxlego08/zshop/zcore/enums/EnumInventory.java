package fr.maxlego08.zshop.zcore.enums;

public enum EnumInventory {

	;
	
	private final int id;

	private EnumInventory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
