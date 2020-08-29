package fr.maxlego08.shop.button;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.zcore.utils.ZUtils;

public class ZButton extends ZUtils implements Button {

	private final ButtonType type;
	private final ItemStack itemStack;
	private final int slot;
	private int tmpSlot;
	private boolean isPermanent;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 */
	public ZButton(ButtonType type, ItemStack itemStack, int slot, boolean isPermanent) {
		super();
		this.type = type;
		this.itemStack = itemStack;
		this.slot = slot;
		this.isPermanent = isPermanent;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public ButtonType getType() {
		return type;
	}

	@Override
	public int getSlot() {
		return slot;
	}

	@Override
	public void setTmpSlot(int slot) {
		if (this.isPermament())
			this.tmpSlot = this.slot;
		else
			this.tmpSlot = slot;
	}

	@Override
	public int getTmpSlot() {
		return tmpSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZButton [type=" + type + ", itemStack=" + itemStack + ", slot=" + slot + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Button> T toButton(Class<T> classz) {
		return (T) this;
	}

	@Override
	public ItemStack getCustomItemStack(Player player) {
		return this.itemStack;
	}

	@Override
	public boolean isClickable() {
		return type.isClickable();
	}

	@Override
	public boolean isPermament() {
		return isPermanent;
	}

}
