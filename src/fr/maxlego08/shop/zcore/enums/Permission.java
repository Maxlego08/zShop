package fr.maxlego08.shop.zcore.enums;

public enum Permission {
	ZSHOP_USE, ZSHOP_RELOAD

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
