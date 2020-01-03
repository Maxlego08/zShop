package fr.maxlego08.shop.zshop.categories;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;

public class Category extends ZUtils {

	private final int id;
	private final int slot;
	private final ShopType type;
	private final String name;
	private final MaterialData data;
	private final List<String> lore;
	private final int inventorySize;
	private final int backButtonSlot;
	private final int previousButtonSlot;
	private final int nexButtonSlot;

	private transient boolean isLoaded = false;

	public Category(int id, int slot, ShopType type, String name, MaterialData data, List<String> lore,
			int inventorySize, int backButtonSlot, int previousButtonSlot, int nexButtonSlot) {
		super();
		this.id = id;
		this.slot = slot;
		this.type = type;
		this.name = name;
		this.data = data;
		this.lore = lore;
		this.inventorySize = inventorySize;
		this.backButtonSlot = backButtonSlot;
		this.previousButtonSlot = previousButtonSlot;
		this.nexButtonSlot = nexButtonSlot;
	}

	/**
	 * @return the backButtonSlot
	 */
	public int getBackButtonSlot() {
		return backButtonSlot;
	}

	/**
	 * @return the previousButtonSlot
	 */
	public int getPreviousButtonSlot() {
		return previousButtonSlot;
	}

	/**
	 * @return the nexButtonSlot
	 */
	public int getNexButtonSlot() {
		return nexButtonSlot;
	}

	/**
	 * @return the inventorySize
	 */
	public int getInventorySize() {
		return inventorySize;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public MaterialData getData() {
		return data;
	}

	/**
	 * @return the lore
	 */
	public List<String> getLore() {
		return lore;
	}

	/**
	 * @return the type
	 */
	public ShopType getType() {
		return type;
	}

	/**
	 * @return the ItemStack
	 */
	public ItemStack toItemStack() {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(data.getTypeMaterial(), 1, (byte) data.getData());
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		if (lore != null)
			itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}

	/**
	 * 
	 * @return true if category can be load
	 */
	public boolean isCorrect() {
		if (nexButtonSlot >= inventorySize)
			return false;
		if (previousButtonSlot >= inventorySize)
			return false;
		if (backButtonSlot >= inventorySize)
			return false;
		if (type == null) {
			Logger.info("ShopType is null ! Set " + ShopType.UNIQUE_ITEM.name() + " or "
								+ ShopType.ITEM.name() + " for category with id " + id, LogType.ERROR);
			return false;
		}
		isLoaded = true;
		return true;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

}
