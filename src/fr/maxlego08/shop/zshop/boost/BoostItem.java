package fr.maxlego08.shop.zshop.boost;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.zcore.utils.ZUtils;

public class BoostItem extends ZUtils{

	private final String item;
	private transient ItemStack itemStack;
	private long ending;
	private final BoostType boostType;
	private final double modifier;
	
	public BoostItem(ItemStack itemStack, long ending, BoostType boostType, double modifier) {
		super();
		this.item = encode(itemStack);
		this.itemStack = itemStack;
		this.ending = ending;
		this.boostType = boostType;
		this.modifier = modifier;
	}
	/**
	 * @return the itemStack
	 */
	public ItemStack getItemStack() {
		if (itemStack == null)
			itemStack = decode(item);
		return itemStack;
	}
	/**
	 * @return the ending
	 */
	public long getEnding() {
		return ending;
	}
	/**
	 * @return the boostType
	 */
	public BoostType getBoostType() {
		return boostType;
	}
	/**
	 * @return the modifier
	 */
	public double getModifier() {
		return modifier;
	}
	
	public boolean reset() {
		return System.currentTimeMillis() >= ending;
	}
	
	public void end(){
		ending = 0;
	}
	
}
