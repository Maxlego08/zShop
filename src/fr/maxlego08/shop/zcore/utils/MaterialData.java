package fr.maxlego08.shop.zcore.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialData extends ZUtils{

	private final int type;
	private final int data;

	public MaterialData(int type) {
		this(type, 0);
	}
	
	@SuppressWarnings("deprecation")
	public MaterialData(Material type) {
		this(type.getId(), 0);
	}

	public MaterialData(int type, int data) {
		this.type = type;
		this.data = data;
	}
	
	@SuppressWarnings("deprecation")
	public MaterialData(Material type, int data) {
		this(type.getId(), data);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the data
	 */
	public int getData() {
		return data;
	}
	
	public Material getTypeMaterial(){
		return getMaterial(type);
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack toItemStack(){
		return new ItemStack(getTypeMaterial(), 1, (byte)data);
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack toItemStack(int amount){
		return new ItemStack(getTypeMaterial(), amount, (byte)data);
	}

}
