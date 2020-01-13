package fr.maxlego08.shop.zcore.utils.enums;

public enum Permission {
	
	SHOP_USE("zshop.use"),
	SHOP_RELOAD("zshop.reload"),
	SHOP_HELP("zshop.help"),
	SHOP_HAND("zshop.hand"),
	SHOP_HAND_ALL("zshop.hand.all"),
	SHOP_ALL("zshop.all"),
	SHOP_BOOST("zshop.boost"),
	SHOP_BOOST_STOP("zshop.boost.stop"),
	SHOP_BOOST_SHOW("zshop.boost.show"),
	SHOP_OPEN("zshop.open.%s"),
	SHOP_OPEN_CONFIRM("zshop.open.confirm"),
	SHOP_OPEN_BUY("zshop.open.buy"),
	SHOP_OPEN_SELL("zshop.open.sell"),
	SHOP_CONFIG("zshop.config"),
	SHOP_CONFIG_ADD_ITEM("zshop.config.add.item"),
	SHOP_CONFIG_EDIT_ITEM("zshop.config.edit.item"),
	SHOP_CONFIG_DELETE_ITEM("zshop.config.delete.item"),
	SHOP_CONFIG_ADD_CATEGORY("zshop.config.add.category"),
	SHOP_CONFIG_DELETE_CATEGORY("zshop.config.delete.category"),
	SHOP_CONFIG_CATEGORIES("zshop.config.categories"),
	SHOP_CONFIG_ITEMS("zshop.config.items"),
	SHOP_DEFAULT("zshop.default"),
	
	;

	private final String permission;

	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public String getPermission(Object object) {
		return String.format(permission, object);
	}
	
}
