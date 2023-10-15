package fr.maxlego08.zshop.zcore.enums;

public enum Permission {
	
	ZSHOP_HELP,
	ZSHOP_RELOAD,
	ZSHOP_LOGS,
	ZSHOP_SELL_ALL,
	ZSHOP_SELL_HAND,
	ZSHOP_SELL_HAND_ALL,

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
