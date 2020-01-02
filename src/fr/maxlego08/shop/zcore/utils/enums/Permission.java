package fr.maxlego08.shop.zcore.utils.enums;

public enum Permission {
	
	SHOP_USE("zshop.use"),
	SHOP_RELOAD("zshop.reload"),
	SHOP_HELP("zshop.help"),
	SHOP_HAND("zshop.hand"),
	SHOP_HAND_ALL("zshop.hand.all"),
	SHOP_ALL("zshop.all"),
	
	;

	private final String permission;

	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
}
