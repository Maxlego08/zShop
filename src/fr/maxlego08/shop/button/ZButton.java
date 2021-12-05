package fr.maxlego08.shop.button;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.sound.SoundOption;
import fr.maxlego08.shop.zcore.utils.ZUtils;

public class ZButton extends ZUtils implements Button {

	private final ButtonType type;
	private final ItemStack itemStack;
	private final int slot;
	private final SoundOption sound;
	private int tmpSlot;
	private boolean isPermanent;
	private boolean isClose;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 */
	public ZButton(ButtonType type, ItemStack itemStack, int slot, boolean isPermanent, SoundOption sound,
			boolean isClose) {
		super();
		this.type = type;
		this.itemStack = itemStack;
		this.slot = slot;
		this.isPermanent = isPermanent;
		this.sound = sound;
		this.isClose = isClose;
	}

	@Override
	public ItemStack getItemStack() {
		return this.itemStack;
	}

	@Override
	public ButtonType getType() {
		return this.type;
	}

	@Override
	public int getSlot() {
		return this.slot;
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
		return this.tmpSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZButton [type=" + this.type + ", itemStack=" + this.itemStack + ", slot=" + this.slot + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Button> T toButton(Class<T> classz) {
		return (T) this;
	}

	@Override
	public ItemStack getCustomItemStack(Player player) {
		ItemStack itemStack = this.itemStack.clone();
		return super.playerHead(super.papi(itemStack, player), player);
	}

	@Override
	public boolean isClickable() {
		return this.type.isClickable() || this.closeInventory();
	}

	@Override
	public boolean isPermament() {
		return this.isPermanent;
	}

	@Override
	public SoundOption getSound() {
		return this.sound;
	}

	@Override
	public void playSound(Entity entity) {
		if (this.sound != null)
			this.sound.play(entity);
	}

	@Override
	public boolean closeInventory() {
		return this.isClose;
	}

}
