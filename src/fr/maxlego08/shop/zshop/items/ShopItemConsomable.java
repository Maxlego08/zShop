package fr.maxlego08.shop.zshop.items;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.event.events.ShopPostBuyEvent;
import fr.maxlego08.shop.event.events.ShopPostSellEvent;
import fr.maxlego08.shop.event.events.ShopPreBuyEvent;
import fr.maxlego08.shop.event.events.ShopPreSellEvent;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.LoggerManager;
import fr.maxlego08.shop.zcore.utils.builder.TimerBuilder;
import fr.maxlego08.shop.zshop.boost.BoostItem;
import fr.maxlego08.shop.zshop.boost.BoostType;
import fr.maxlego08.shop.zshop.factories.Boost;
import fr.maxlego08.shop.zshop.utils.Action;
import fr.maxlego08.shop.zshop.utils.ShopAction;

public class ShopItemConsomable extends EconomyUtils implements ShopItem {

	private final Economy economy;
	private final int id;
	private final ItemStack itemStack;
	private int slot;
	private int tmpSlot;
	private double sellPrice;
	private double buyPrice;
	private final int maxStackSize;
	private boolean giveItem = true;
	private boolean executeSellCommand = true;
	private boolean executeBuyCommand = true;
	private List<String> commands = new ArrayList<>();

	protected Boost boost = ZShop.i().getBoost();

	/**
	 * 
	 * @param id
	 * @param itemStack
	 * @param slot
	 * @param sellPrice
	 * @param buyPrice
	 * @param maxStackSize
	 */
	public ShopItemConsomable(int id, ItemStack itemStack, int slot, double sellPrice, double buyPrice,
			int maxStackSize) {
		super();
		this.economy = Economy.VAULT;
		this.id = id;
		this.itemStack = itemStack;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.slot = slot;
		this.maxStackSize = maxStackSize;
	}

	/**
	 * 
	 * @param economy
	 * @param id
	 * @param itemStack
	 * @param slot
	 * @param sellPrice
	 * @param buyPrice
	 * @param maxStackSize
	 * @param giveItem
	 * @param executeSellCommand
	 * @param executeBuyCommand
	 * @param commands
	 */
	public ShopItemConsomable(Economy economy, int id, ItemStack itemStack, int slot, double sellPrice, double buyPrice,
			int maxStackSize, boolean giveItem, boolean executeSellCommand, boolean executeBuyCommand,
			List<String> commands) {
		super();
		this.economy = economy;
		this.id = id;
		this.itemStack = itemStack;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.slot = slot;
		this.maxStackSize = maxStackSize;
		this.giveItem = giveItem;
		this.executeSellCommand = executeSellCommand;
		this.executeBuyCommand = executeBuyCommand;
		this.commands = commands;
	}

	/**
	 * @return the tmpSlot
	 */
	public int getTmpSlot() {
		return tmpSlot;
	}

	/**
	 * @param tmpSlot
	 *            the tmpSlot to set
	 */
	public void setTmpSlot(int tmpSlot) {
		this.tmpSlot = tmpSlot;
	}

	public List<String> getCommands() {
		return commands;
	}

	public boolean isGiveItem() {
		return giveItem;
	}

	@Override
	public ShopType getType() {
		return ShopType.ITEM;
	}

	@Override
	public int getCategory() {
		return id;
	}

	@Override
	public int getSlot() {
		return slot;
	}

	@Override
	public void performBuy(Player player, int amount) {
		if (amount <= 0)
			amount = 1;

		double currentBuyPrice = getBuyPrice();

		if (currentBuyPrice <= 0)
			return;

		double currentPrice = currentBuyPrice * amount;
		if (!this.hasMoney(economy, player, currentPrice)) {
			player.sendMessage(Lang.prefix + " " + Lang.notEnouhtMoney);
			return;
		}

		if (hasInventoryFull(player)) {
			player.sendMessage(Lang.prefix + " " + Lang.notEnouhtPlace);
			return;
		}

		if (Config.shopPreBuyEvent) {
			ShopPreBuyEvent event = new ShopPreBuyEvent(this, player, amount, currentBuyPrice);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;

			currentBuyPrice = event.getPrice();
			amount = event.getQuantity();
		}

		withdrawMoney(economy, player, currentPrice);
		ItemStack currentItem = itemStack.clone();
		currentItem.setAmount(amount);
		if (giveItem)
			give(player, currentItem);

		player.sendMessage(Lang.prefix + " "
				+ Lang.buyItem.replace("%currency%", economy.toCurrency()).replace("%amount%", String.valueOf(amount))
						.replace("%item%", currentItem.getType().name().toLowerCase().replace("_", " "))
						.replace("%price%", format(currentPrice)));

		// Log
		ShopAction shopAction = new ShopAction(Action.BOUGHT, player, currentItem, amount, currentPrice, economy);
		LoggerManager.log(shopAction);

		if (Config.shopPostBuyEvent) {
			ShopPostBuyEvent shopPostBuyEvent = new ShopPostBuyEvent(this, player, amount, currentBuyPrice);
			Bukkit.getPluginManager().callEvent(shopPostBuyEvent);
		}

		if (commands.size() != 0 && executeBuyCommand)
			for (String command : commands)
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						command.replace("%currency%", economy.toCurrency()).replace("%player%", player.getName())
								.replace("%amount%", String.valueOf(amount)).replace("%item%", getItemName(itemStack))
								.replace("%price%", format(currentBuyPrice)));

	}

	@Override
	public void performSell(Player player, int amount) {
		int item = 0;

		// On d�finie le nombre d'item dans l'inventaire du joueur
		for (ItemStack is : player.getInventory().getContents()) {
			if (is != null && is.isSimilar(itemStack)) {
				item += is.getAmount();
			}
		}

		// On verif si le joueur � bien l'item
		if (item <= 0) {
			player.sendMessage(Lang.prefix + " " + Lang.notItems);
			return;
		}

		// On verif que le joueur ne veut pas vendre plus qu'il poss�de
		if (item < amount) {
			player.sendMessage(Lang.prefix + " " + Lang.notEnouhtItems);
			return;
		}

		// On d�finie le nombre d'item a vendre en fonction du nombre d'item que
		// le joueur peut vendre
		item = amount == 0 ? item : item < amount ? amount : amount > item ? item : amount;
		int realAmount = item;

		// On cr�er le prix
		double currentSellPrice = getSellPrice();
		double price = realAmount * currentSellPrice;

		/* On appel l'event */

		if (Config.shopPreSellEvent) {
			ShopPreSellEvent event = new ShopPreSellEvent(this, player, realAmount, price);

			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;

			price = event.getSellPrice();
			realAmount = item = event.getQuantity();
		}

		/* Fin de l'event */

		int slot = 0;

		// On retire ensuite les items de l'inventaire du joueur
		for (ItemStack is : player.getInventory().getContents()) {

			if (is != null && is.isSimilar(itemStack) && item > 0) {

				int currentAmount = is.getAmount() - item;
				item -= is.getAmount();

				if (currentAmount <= 0) {
					if (slot == 40)
						player.getInventory().setItemInOffHand(null);
					else
						player.getInventory().removeItem(is);
				} else
					is.setAmount(currentAmount);
			}
			slot++;
		}

		// On termine l'action
		this.depositMoney(economy, player, price);
		message(player,
				Lang.sellItem.replace("%currency%", economy.toCurrency())
						.replace("%amount%", String.valueOf(realAmount)).replace("%item%", getItemName(itemStack))
						.replace("%price%", format(price)));

		// Log
		ShopAction action = new ShopAction(Action.SELL, player, itemStack, realAmount, price, economy);
		LoggerManager.log(action);

		/**
		 * Appel de l'event
		 */
		if (Config.shopPostSellEvent) {
			ShopPostSellEvent eventPost = new ShopPostSellEvent(this, player, realAmount, price);
			Bukkit.getPluginManager().callEvent(eventPost);
		}

		if (commands.size() != 0 && executeSellCommand)
			for (String command : commands)
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						command.replace("%currency%", economy.toCurrency()).replace("%player%", player.getName())
								.replace("%amount%", String.valueOf(realAmount))
								.replace("%item%", getItemName(itemStack)).replace("%price%", format(price)));

	}

	@Override
	public ItemStack getItem() {
		if (itemStack == null)
			throw new IllegalArgumentException(
					"L'item est null dans la category " + id + " avec les prix: " + sellPrice + " - " + buyPrice);
		return itemStack;
	}

	@Override
	public double getSellPrice() {

		if (sellPrice == 0)
			return sellPrice;

		// On verifie si l'item est boost ou pas
		if (boost.isBoost(itemStack)) {
			BoostItem boostItem = boost.getBoost(itemStack);
			if (boostItem.getBoostType().equals(BoostType.SELL))
				return boostItem.getModifier() * sellPrice;
		}
		return sellPrice;
	}

	@Override
	public double getBuyPrice() {
		if (buyPrice == 0)
			return buyPrice;

		// On verifie si l'item est boost ou pas
		if (boost.isBoost(itemStack)) {
			BoostItem boostItem = boost.getBoost(itemStack);
			if (boostItem.getBoostType().equals(BoostType.BUY))
				return boostItem.getModifier() * buyPrice;
		}
		return buyPrice;
	}

	@Override
	public ItemStack getDisplayItem() {
		ItemStack itemStack = this.itemStack.clone();
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<String>();
		List<String> tmpLore = Lang.displayItemLore.stream().map(string -> {

			String str = string.replace("%buyPrice%", getBuyPriceAsString());

			if (!str.equals(string))
				str = str.replace("%currency%", isBuyable() ? economy.toCurrency() : "");

			str = str.replace("%sellPrice%", getSellPriceAsString());
			if (!str.equals(string))
				str = str.replace("%currency%", isSellable() ? economy.toCurrency() : "");

			return str;

		}).collect(Collectors.toList());
		lore.addAll(tmpLore);

		List<String> tmpLore2 = new ArrayList<>();
		for (String string : lore) {
			if (string.equalsIgnoreCase("%boostinfo%")) {

				if (boost.isBoost(this.itemStack)) {
					BoostItem boost = this.boost.getBoost(itemStack);
					if (boost != null) {
						String tmpStr = TimerBuilder
								.getStringTime(Math.abs((boost.getEnding() - System.currentTimeMillis())) / 1000);
						tmpLore2.addAll(Lang.displayItemLoreBoost.stream()
								.map(s -> s.replace("%currency%", economy.toCurrency()).replace("%ending%", tmpStr)
										.replace("%modifier%", format(boost.getModifier())))
								.collect(Collectors.toList()));
					} else
						tmpLore2.add("");
				} else
					tmpLore2.add("");
			} else
				tmpLore2.add(string);
		}
		itemMeta.setLore(tmpLore2);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	private String getSellPriceAsString() {
		if (boost.isBoost(itemStack) && isSellable()) {
			BoostItem boostItem = boost.getBoost(itemStack);
			if (boostItem.getBoostType().equals(BoostType.SELL))
				return Lang.boostItemSell.replace("%currency%", economy.toCurrency())
						.replace("%defaultPrice%", String.valueOf(sellPrice))
						.replace("%newPrice%", format(sellPrice * boostItem.getModifier()));
		}
		return isSellable() ? String.valueOf(sellPrice) : Lang.canSell;
	}

	private String getBuyPriceAsString() {
		if (boost.isBoost(itemStack) && isBuyable()) {
			BoostItem boostItem = boost.getBoost(itemStack);
			if (boostItem.getBoostType().equals(BoostType.BUY))
				return Lang.boostItemBuy.replace("%currency%", economy.toCurrency())
						.replace("%defaultPrice%", String.valueOf(buyPrice))
						.replace("%newPrice%", format(buyPrice * boostItem.getModifier()));
		}
		return isBuyable() ? String.valueOf(buyPrice) : Lang.canBuy;
	}

	@Override
	public int getMaxStackSize() {
		return maxStackSize;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the itemStack
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * @return the executeSellCommand
	 */
	public boolean isExecuteSellCommand() {
		return executeSellCommand;
	}

	/**
	 * @return the executeBuyCommand
	 */
	public boolean isExecuteBuyCommand() {
		return executeBuyCommand;
	}

	/**
	 * @param giveItem
	 *            the giveItem to set
	 */
	public void setGiveItem(boolean giveItem) {
		this.giveItem = giveItem;
	}

	/**
	 * @param executeSellCommand
	 *            the executeSellCommand to set
	 */
	public void setExecuteSellCommand(boolean executeSellCommand) {
		this.executeSellCommand = executeSellCommand;
	}

	/**
	 * @param executeBuyCommand
	 *            the executeBuyCommand to set
	 */
	public void setExecuteBuyCommand(boolean executeBuyCommand) {
		this.executeBuyCommand = executeBuyCommand;
	}

	/**
	 * @param commands
	 *            the commands to set
	 */
	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	@Override
	public boolean useConfirm() {
		return false;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	@Override
	public Economy getEconomyType() {
		return economy;
	}

	@Override
	public boolean isSellable() {
		return sellPrice != 0;
	}

	@Override
	public boolean isBuyable() {
		return buyPrice != 0;
	}

}
