package fr.maxlego08.shop.zshop.categories;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;

public class Category extends ZUtils {

	private final int id;
	private final int slot;
	private final ShopType type;
	private final String name;
	private final int itemId;
	private final List<String> lore;
	private final int inventorySize;
	private final int backButtonSlot;
	private final int previousButtonSlot;
	private final int nexButtonSlot;

	public Category(int id, int slot, ShopType type, String name, int itemId, List<String> lore, int inventorySize,
			int backButtonSlot, int previousButtonSlot, int nexButtonSlot) {
		super();
		this.id = id;
		this.slot = slot;
		this.type = type;
		this.name = name;
		this.itemId = itemId;
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

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
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

	public ItemStack toItemStack() {
		ItemStack item = new ItemStack(getMaterial(itemId));
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		if (lore != null)
			itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}

}
