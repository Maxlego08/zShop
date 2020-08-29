package fr.maxlego08.shop.zcore.utils;

import java.util.Optional;

import fr.maxlego08.shop.api.permission.Permission;

public class TemporyObject {

	private final long expireAt;
	private final Optional<Permission> permission;

	/**
	 * @param createdAt
	 * @param permission
	 */
	public TemporyObject(Optional<Permission> permission) {
		super();
		this.expireAt = System.currentTimeMillis() + 1000 * 5;
		this.permission = permission;
	}

	/**
	 * @return the permission
	 */
	public Optional<Permission> getPermission() {
		return permission;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > expireAt;
	}

}
