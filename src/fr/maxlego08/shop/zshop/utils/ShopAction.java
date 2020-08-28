package fr.maxlego08.shop.zshop.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zshop.items.Economy;

public class ShopAction extends ZUtils {

	private final Action action;
	private final OfflinePlayer player;
	private final ItemStack itemName;
	private long amount;
	private double price;
	private final Economy economy;
	private final String date;

	public ShopAction(Action action, OfflinePlayer player, ItemStack itemName, long amount, double price,
			Economy economy) {
		super();
		this.action = action;
		this.player = player;
		this.itemName = itemName;
		this.amount = amount;
		this.price = price;
		this.economy = economy;
		SimpleDateFormat datenow = new SimpleDateFormat("HH:mm:ss.SSS", Locale.FRANCE);
		this.date = datenow.format(new Date());
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @return the name
	 */
	public OfflinePlayer getName() {
		return player;
	}

	/**
	 * @return the itemStack
	 */
	public long getItemStack() {
		return amount;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the economy
	 */
	public Economy getEconomyType() {
		return economy;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	public ItemStack getItemName() {
		return itemName;
	}

	/**
	 * 
	 * @return
	 */
	public String toMessage() {
		String name = player.getName();
		return name + " just " + name(action.name()) + " x" + amount + " " + getItemName(itemName) + " for "
				+ getPriceString() + " " + economy.toCurrency();
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + (int) (amount ^ (amount >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((economy == null) ? 0 : economy.hashCode());
		result = prime * result + ((itemName == null) ? 0 : itemName.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShopAction other = (ShopAction) obj;
		if (action != other.action)
			return false;
		if (economy != other.economy)
			return false;
		if (itemName == null) {
			if (other.itemName != null)
				return false;
		} else if (!itemName.isSimilar(other.itemName))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.getUniqueId().equals(other.player.getUniqueId()))
			return false;
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public String getPriceString() {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(price);
	}

	/**
	 * 
	 * @param action
	 */
	public void add(ShopAction action) {
		this.amount += action.amount;
		this.price += action.price;
	}

}
