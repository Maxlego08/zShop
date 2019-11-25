package fr.maxlego08.shop.zshop.items;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.event.events.ShopPostBuyEvent;
import fr.maxlego08.shop.event.events.ShopPostSellEvent;
import fr.maxlego08.shop.event.events.ShopPreBuyEvent;
import fr.maxlego08.shop.event.events.ShopPreSellEvent;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.ZUtils;

public class ShopItemConsomable extends ZUtils implements ShopItem {

	private final int id;
	private final ItemStack itemStack;
	private final double sellPrice;
	private final double buyPrice;
	private final int maxStackSize;

	/**
	 * Création de l'item
	 * @param id -> de l'item
	 * @param itemStack -> item a vendre
	 * @param sellPrice -> prix de vente, si 0 alors invendable 
	 * @param buyPrice -> prix d'achat, si 0 alors inchatable
	 * @param maxStackSize -> nombre maximum d'achat ou de vente
	 */
	public ShopItemConsomable(int id, ItemStack itemStack, double sellPrice, double buyPrice, int maxStackSize) {
		super();
		this.id = id;
		this.itemStack = itemStack;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.maxStackSize = maxStackSize;
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
		return 0;
	}

	@Override
	public void performBuy(Player player, int amount) {
		if (amount <= 0)
			amount = 1;

		double currentBuyPrice = buyPrice;

		if (currentBuyPrice <= 0)
			return;

		double currentPrice = currentBuyPrice * amount;
		if (getBalance(player) < currentPrice) {
			player.sendMessage(Lang.prefix + " " + Lang.notEnouhtMoney);
			return;
		}

		if (hasInventoryFull(player)){
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

		withdrawMoney(player, currentPrice);
		ItemStack currentItem = itemStack.clone();
		currentItem.setAmount(amount);
		give(player, currentItem);

		player.sendMessage(Lang.prefix + " "
				+ Lang.sellItem.replace("%amount%", String.valueOf(amount)).replace("%item%", currentItem.getType().name().toLowerCase().replace("_", " "))
						.replace("%price%", format(currentPrice)));

		Logger.info(player.getName() + " vient d'acheter x" + amount + " "
				+ currentItem.getType().name().toLowerCase().replace("_", " ") + " pour " + format(currentPrice) + "$",
				LogType.INFO);

		if (Config.shopPostBuyEvent) {
			ShopPostBuyEvent shopPostBuyEvent = new ShopPostBuyEvent(this, player, amount, currentBuyPrice);
			Bukkit.getPluginManager().callEvent(shopPostBuyEvent);
		}

	}

	@Override
	public void performSell(Player player, int amount) {
		int item = 0;
		final Material currentMaterial = itemStack.getType();
		ItemStack[] arrayOfItemStack;

		// On définie le nombre d'item que le joueur peut vendre

		int x = (arrayOfItemStack = player.getInventory().getContents()).length;
		for (int i = 0; i < x; i++) {
			ItemStack contents = arrayOfItemStack[i];
			if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == currentMaterial))
				item = item + contents.getAmount();
		}
		if (item == 0) {
			player.sendMessage(Lang.prefix + " " + Lang.notItems);
			return;
		}
		if (item < amount) {
			player.sendMessage(Lang.prefix + " " + Lang.notEnouhtItems);
			return;
		}
		// On définie le nombre d'item a vendre en fonction du nombre d'item que
		// le joueur peut vendre
		item = amount == 0 ? item : item < amount ? amount : amount > item ? item : amount;
		int realAmount = item;

		// On créer le prix
		double currentSellPrice = sellPrice;
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

		// On retire ensuite les items de l'inventaire du joueur
		for (int i = 0; i < x; i++) {
			ItemStack contents = arrayOfItemStack[i];
			if ((contents != null) && (contents.getType() != Material.AIR) && (contents.getType() == currentMaterial)) {
				if (item <= 0)
					continue;
				if (contents.getAmount() >= 64 && item >= 64) {
					player.getInventory().remove(contents.getType());
					item -= 64;
				} else if (contents.getAmount() == item) {
					player.getInventory().remove(contents.getType());
					item -= contents.getAmount();
				} else {
					int diff = contents.getAmount() - item;
					contents.setAmount(diff);
					item -= diff;
				}
			}
		}

		// On termine l'action
		depositMoney(player, price);
		player.sendMessage(Lang.prefix + " "
				+ Lang.sellItem.replace("%amount%", String.valueOf(realAmount)).replace("%item%", itemName(itemStack))
						.replace("%price%", format(price)));
		Logger.info(
				player.getName() + " just sold x" + realAmount + " "
						+ currentMaterial.name().toLowerCase().replace("_", " ") + " for " + format(price) + "$",
				LogType.INFO);

		/**
		 * Appel de l'event
		 */
		if (Config.shopPostSellEvent) {
			ShopPostSellEvent eventPost = new ShopPostSellEvent(this, player, realAmount, price);
			Bukkit.getPluginManager().callEvent(eventPost);
		}

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

	@Override
	public ItemStack getDisplayItem() {
		ItemStack itemStack = this.itemStack.clone();
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<String>();
		List<String> tmpLore = Lang.displayItemLore.stream().map(string -> string
				.replace("%buyPrice%", String.valueOf(buyPrice)).replace("%sellPrice%", String.valueOf(sellPrice)))
				.collect(Collectors.toList());
		lore.addAll(tmpLore);
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	@Override
	public int getMaxStackSize() {
		return maxStackSize;
	}

}
