package fr.maxlego08.shop.zshop.categories;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.zcore.utils.ZUtils;

public class Category extends ZUtils {

	private final int id;
	private final int slot;
	private final String name;
	private final int itemId;
	private final List<String> lore;

	public Category(int id, int slot, String name, int itemId, List<String> lore) {
		super();
		this.id = id;
		this.slot = slot;
		this.name = name;
		this.itemId = itemId;
		this.lore = lore;
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
