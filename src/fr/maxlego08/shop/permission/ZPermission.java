package fr.maxlego08.shop.permission;

import fr.maxlego08.shop.api.enums.PermissionType;
import fr.maxlego08.shop.api.permission.Permission;

public class ZPermission implements Permission {

	private final PermissionType type;
	private final double percent;
	private final String permission;

	/**
	 * @param type
	 * @param percent
	 * @param permission
	 */
	public ZPermission(PermissionType type, double percent, String permission) {
		super();
		this.type = type;
		this.percent = percent;
		this.permission = permission;
	}

	/**
	 * @return the type
	 */
	public PermissionType getType() {
		return type;
	}

	/**
	 * @return the percent
	 */
	public double getPercent() {
		return percent;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZPermission [type=" + type + ", percent=" + percent + ", permission=" + permission + "]";
	}

}
