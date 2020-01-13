package fr.maxlego08.shop.zcore.utils.inventory;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.zcore.utils.ZUtils;

public class Button extends ZUtils {

	private final String name;
	private final int item;
	private final int data;
	private final List<String> lore;

	public Button(String name, int item, int data, List<String> lore) {
		this.name = name;
		this.lore = lore;
		this.item = item;
		this.data = data;
	}

	public Button(String name, int item, int data) {
		this.name = name;
		this.item = item;
		this.data = data;
		this.lore = null;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	public int getData() {
		return data;
	}

	public int getItemInInteger() {
		return item;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItem() {
		return new ItemStack(getMaterial(item), 1, (byte) data);
	}

	public boolean hasLore() {
		return lore != null;
	}

	public boolean hasName() {
		return name != null;
	}

	public ItemStack getInitButton() {
		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		if (hasName())
			itemM.setDisplayName(getName());
		if (hasLore())
			itemM.setLore(getLore());
		item.setItemMeta(itemM);
		return item;
	}

	public ItemStack getInitButton(String type, String replace) {
		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		if (hasName())
			itemM.setDisplayName(getName().replace(type, replace));
		if (hasLore())
			itemM.setLore(getLore());
		item.setItemMeta(itemM);
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Button [name=" + name + ", item=" + item + ", data=" + data + ", lore=" + lore + ", item="
				+ getInitButton() + "]";
	}

}
