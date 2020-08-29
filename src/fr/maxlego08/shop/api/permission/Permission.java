package fr.maxlego08.shop.api.permission;

import fr.maxlego08.shop.api.enums.PermissionType;

public interface Permission {

	/**
	 * 
	 * @return type
	 */
	public PermissionType getType();
	
	/**
	 * 
	 * @return permission
	 */
	public String getPermission();
	
	/**
	 * 
	 * @return percent
	 */
	public double getPercent();
	
}
