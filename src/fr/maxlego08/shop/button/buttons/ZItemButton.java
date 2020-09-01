package fr.maxlego08.shop.button.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.api.enums.PermissionType;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.events.ZShopBuyEvent;
import fr.maxlego08.shop.api.events.ZShopSellEvent;
import fr.maxlego08.shop.api.permission.Permission;
import fr.maxlego08.shop.save.Lang;

public class ZItemButton extends ZPlaceholderButton implements ItemButton {

	private final ShopManager manager;
	private final IEconomy iEconomy;
	private final double sellPrice;
	private final double buyPrice;
	private final int maxStack;
	private final Economy economy;
	private final List<String> lore;
	private final List<String> buyCommands;
	private final List<String> sellCommands;
	private final boolean giveItem;

	/**
	 * 
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param isPermanent
	 * @param action
	 * @param placeholder
	 * @param value
	 * @param manager
	 * @param iEconomy
	 * @param sellPrice
	 * @param buyPrice
	 * @param maxStack
	 * @param economy
	 * @param lore
	 * @param buyCommands
	 * @param sellCommands
	 * @param giveItem
	 */
	public ZItemButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, double value,
			ShopManager manager, IEconomy iEconomy, double sellPrice, double buyPrice, int maxStack, Economy economy,
			List<String> lore, List<String> buyCommands, List<String> sellCommands, boolean giveItem) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value);
		this.manager = manager;
		this.iEconomy = iEconomy;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.maxStack = maxStack;
		this.economy = economy;
		this.lore = lore;
		this.buyCommands = buyCommands;
		this.sellCommands = sellCommands;
		this.giveItem = giveItem;
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
	public boolean canSell() {
		return sellPrice != 0.0;
	}

	@Override
	public boolean canBuy() {
		return buyPrice != 0;
	}

	@Override
	public Economy getEconomy() {
		return economy == null ? Economy.VAULT : economy;
	}

	@Override
	public boolean needToConfirm() {
		return super.getType() == ButtonType.ITEM_CONFIRM;
	}

	@Override
	public IEconomy getIEconomy() {
		return iEconomy;
	}

	@Override
	public void buy(Player player, int amount) {

		double currentPrice = this.getBuyPrice(player) * amount;

		if (currentPrice < 0)
			return;

		if (!iEconomy.hasMoney(economy, player, currentPrice)) {
			message(player, Lang.notEnouhtMoney);
			return;
		}

		if (hasInventoryFull(player)) {
			message(player, Lang.notEnouhtPlace);
			return;
		}

		ZShopBuyEvent event = new ZShopBuyEvent(this, player, amount, currentPrice, economy);
		event.callEvent();

		if (event.isCancelled())
			return;

		Economy economy = event.getEconomy();
		currentPrice = event.getPrice();
		amount = event.getAmount();

		iEconomy.withdrawMoney(economy, player, currentPrice);

		ItemStack itemStack = super.getItemStack().clone();
		itemStack.setAmount(amount);

		if (this.giveItem)
			give(player, itemStack);

		String message = Lang.buyItem;
		message = message.replace("%amount%", String.valueOf(amount));
		message = message.replace("%item%", getItemName(itemStack));
		message = message.replace("%price%", format(currentPrice));
		message = message.replace("%currency%", this.economy.getCurrenry());

		message(player, message);

		if (buyCommands.size() != 0)
			for (String command : buyCommands) {
				command = command.replace("%amount%", String.valueOf(amount));
				command = command.replace("%item%", getItemName(itemStack));
				command = command.replace("%price%", format(currentPrice));
				command = command.replace("%currency%", this.economy.getCurrenry());
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(command, player));
			}

	}

	@Override
	public void sell(Player player, int amount) {

		ItemStack itemStack = super.getItemStack().clone();
		int item = 0;

		for (ItemStack is : player.getInventory().getContents())
			if (is != null && is.isSimilar(itemStack))
				item += is.getAmount();

		if (item <= 0) {
			message(player, Lang.notItems);
			return;
		}

		if (item < amount) {
			message(player, Lang.notEnouhtItems);
			return;
		}

		item = amount == 0 ? item : item < amount ? amount : amount > item ? item : amount;
		int realAmount = item;

		double currentPrice = this.getSellPrice(player) * realAmount;

		int slot = 0;

		ZShopSellEvent event = new ZShopSellEvent(this, player, realAmount, currentPrice, economy);
		event.callEvent();

		if (event.isCancelled())
			return;

		currentPrice = event.getPrice();
		Economy economy = event.getEconomy();
		realAmount = event.getAmount();

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

		player.updateInventory();

		this.iEconomy.depositMoney(economy, player, currentPrice);

		String message = Lang.sellItem;
		message = message.replace("%amount%", String.valueOf(realAmount));
		message = message.replace("%item%", getItemName(itemStack));
		message = message.replace("%price%", format(currentPrice));
		message = message.replace("%currency%", this.economy.getCurrenry());

		message(player, message);

		if (sellCommands.size() != 0)
			for (String command : sellCommands) {
				command = command.replace("%amount%", String.valueOf(amount));
				command = command.replace("%item%", getItemName(itemStack));
				command = command.replace("%price%", format(currentPrice));
				command = command.replace("%currency%", this.economy.getCurrenry());
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(command, player));
			}

	}

	@Override
	public int getMaxStack() {
		return maxStack;
	}

	@Override
	public List<String> getLore() {
		return lore;
	}

	@Override
	public ItemStack createItemStack(Player player) {

		ItemStack itemStack = super.getItemStack().clone();
		ItemMeta itemMeta = itemStack.getItemMeta();

		if (itemMeta == null)
			return itemStack;

		Optional<Permission> optionalBuy = manager.getPermission(player, PermissionType.BUY);
		Optional<Permission> optionalSell = manager.getPermission(player, PermissionType.SELL);

		List<String> lore = new ArrayList<String>();

		if (itemMeta.hasLore())
			lore.addAll(itemMeta.getLore());

		this.lore.forEach(string -> {

			String str = string.replace("%buyPrice%", this.getBuyPriceAsString(optionalBuy, 1));
			if (!str.equals(string))
				str = str.replace("%currency%", this.canBuy() ? this.economy.getCurrenry() : "");

			str = str.replace("%sellPrice%", this.getSellPriceAsString(optionalSell, 1));
			if (!str.equals(string))
				str = str.replace("%currency%", this.canSell() ? this.economy.getCurrenry() : "");
			str = str.replace("&", "§");

			if (optionalBuy.isPresent() && this.canBuy())
				str = str.replace("%buyPermission%",
						Lang.buyPermission.replace("%percent%", format(optionalBuy.get().getPercent())));
			else
				str = str.replace("%buyPermission%", "");

			if (optionalSell.isPresent() && this.canSell())
				str = str.replace("%sellPermission%",
						Lang.sellPermission.replace("%percent%", format(optionalSell.get().getPercent())));
			else
				str = str.replace("%sellPermission%", "");

			if ((str.contains("%buyPermissionLine%") && (!optionalBuy.isPresent() || !this.canBuy()))
					|| (str.contains("%sellPermissionLine%") && (!optionalBuy.isPresent() || !this.canSell()))) {
				return;
			}

			if (str.contains("%buyPermissionLine%") && optionalBuy.isPresent() && this.canBuy())
				str = Lang.buyPermissionLine.replace("%percent%", format(optionalSell.get().getPercent()));
			else if (str.contains("%sellPermissionLine%") && optionalSell.isPresent() && this.canSell())
				str = Lang.sellPermissionLine.replace("%percent%", format(optionalSell.get().getPercent()));

			lore.add(str);
		});
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return playerHead(papi(itemStack, player), player);
	}

	@Override
	public ItemStack getCustomItemStack(Player player) {
		return this.createItemStack(player);
	}

	@Override
	public String getSellPriceAsString(Player player, int amount) {
		return this.canSell() ? String.valueOf(getSellPrice(player) * amount) : Lang.canSell;
	}

	@Override
	public String getBuyPriceAsString(Player player, int amount) {
		return this.canBuy() ? String.valueOf(getBuyPrice(player) * amount) : Lang.canBuy;
	}

	@Override
	public boolean giveItem() {
		return giveItem;
	}

	@Override
	public List<String> getBuyCommands() {
		return buyCommands;
	}

	@Override
	public List<String> getSellCommands() {
		return sellCommands;
	}

	public double getSellPrice(Optional<Permission> optional) {
		if (!optional.isPresent())
			return this.getSellPrice();
		double sellPrice = this.getSellPrice();
		return sellPrice + percentNum(sellPrice, optional.get().getPercent());
	}

	public double getBuyPrice(Optional<Permission> optional) {
		if (!optional.isPresent())
			return this.getBuyPrice();
		double buyPrice = this.getBuyPrice();
		return buyPrice - percentNum(buyPrice, optional.get().getPercent());
	}

	public String getSellPriceAsString(Optional<Permission> optional, int amount) {
		return this.canSell() ? String.valueOf(getSellPrice(optional) * amount) : Lang.canSell;
	}

	public String getBuyPriceAsString(Optional<Permission> optional, int amount) {
		return this.canBuy() ? String.valueOf(getBuyPrice(optional) * amount) : Lang.canBuy;
	}

	@Override
	public double getSellPrice(Player player) {
		Optional<Permission> optional = manager.getPermission(player, PermissionType.SELL);
		return this.getSellPrice(optional);
	}

	@Override
	public double getBuyPrice(Player player) {
		Optional<Permission> optional = manager.getPermission(player, PermissionType.BUY);
		return getBuyPrice(optional);
	}

}
