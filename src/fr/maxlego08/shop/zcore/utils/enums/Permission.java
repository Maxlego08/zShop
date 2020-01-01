package fr.maxlego08.shop.zcore.utils.enums;

public enum Permission {
	
	SHOP_USE("zshop.use"),
	SHOP_RELOAD("zshop.reload"),
	SHOP_HELP("zshop.help"),
	
	;

	private final String permission;

	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
}
