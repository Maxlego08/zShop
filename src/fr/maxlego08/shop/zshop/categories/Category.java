package fr.maxlego08.shop.zshop.categories;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zshop.factories.ShopItem.ShopType;

public class Category extends ZUtils {

	private final int id;
	private final int slot;
	private final int inventoryId;
	private final ShopType type;
	private final String name;
	private final ItemStack data;
	private final int inventorySize;
	private final int backButtonSlot;
	private final int previousButtonSlot;
	private final int nexButtonSlot;

	private transient boolean isLoaded = false;

	public Category(int id, int slot, int inventoryId, ShopType type, String name, ItemStack data,
			int inventorySize, int backButtonSlot, int previousButtonSlot, int nexButtonSlot) {
		super();
		this.id = id;
		this.slot = slot;
		this.type = type;
		this.inventoryId = inventoryId;
		this.name = name;
		this.data = data;
		this.inventorySize = inventorySize;
		this.backButtonSlot = backButtonSlot;
		this.previousButtonSlot = previousButtonSlot;
		this.nexButtonSlot = nexButtonSlot;
	}

	public int getInventoryId() {
		return inventoryId;
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

	public ItemStack getData() {
		return data;
	}

	/**
	 * @return the type
	 */
	public ShopType getType() {
		return type;
	}

	public boolean isItem(){
		return type.equals(ShopType.ITEM) || type.equals(ShopType.ITEM_SLOT);
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
