package fr.maxlego08.shop.zshop.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopItemConsomable implements ShopItem {

	private final int id;
	private final ItemStack itemStack;
	private final double sellPrice;
	private final double buyPrice;

	public ShopItemConsomable(int id, ItemStack itemStack, double sellPrice, double buyPrice) {
		super();
		this.id = id;
		this.itemStack = itemStack;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
	}

	@Override
	public ShopType getType() {
		return ShopType.UNIQUE_ITEM;
	}

	@Override
	public int getCategory() {
		return id;
	}

	@Override
	public int getSlot() {
		return 0;
	}

	@Override
	public void performBuy(Player player, int amount) {

	}

	@Override
	public void performSell(Player player, int amount) {

	}

	@Override
	public ItemStack getItem() {
		return itemStack;
	}

	@Override
	public double getSellPrice() {
		return sellPrice;
	}

	@Override
	public double getBuyPrice() {
		return buyPrice;
	}

}
