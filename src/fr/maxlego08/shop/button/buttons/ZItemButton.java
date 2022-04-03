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
import fr.maxlego08.shop.api.history.History;
import fr.maxlego08.shop.api.history.HistoryManager;
import fr.maxlego08.shop.api.history.HistoryType;
import fr.maxlego08.shop.api.permission.Permission;
import fr.maxlego08.shop.api.sound.SoundOption;
import fr.maxlego08.shop.history.ZHistory;
import fr.maxlego08.shop.zcore.enums.Message;

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
	protected final boolean log;
	protected final HistoryManager historyManager;

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
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, String value,
			ShopManager manager, IEconomy iEconomy, double sellPrice, double buyPrice, int maxStack, Economy economy,
			List<String> lore, List<String> buyCommands, List<String> sellCommands, boolean giveItem, boolean glow,
			boolean log, SoundOption sound, boolean isClose) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value, glow,
				sound, isClose);
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
		this.historyManager = manager.getHistory();
		this.log = log;
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
			message(player, this.economy.getDenyMessage());
			return;
		}

		if (hasInventoryFull(player)) {
			message(player, Message.NOT_ENOUGH_PLACE);
			return;
		}

		ZShopBuyEvent event = new ZShopBuyEvent(this, player, amount, currentPrice, economy);
		event.callEvent();

		if (event.isCancelled())
			return;

		Economy economy = event.getEconomy();
		currentPrice = event.getPrice();
		amount = event.getAmount();

		this.iEconomy.withdrawMoney(economy, player, currentPrice);

		ItemStack itemStack = super.getItemStack().clone();
		itemStack.setAmount(amount);

		if (this.giveItem) {
			give(player, papi(itemStack, player));
		}

		message(player, Message.BUY_ITEM, "%amount%", String.valueOf(amount), "%item%", getItemName(itemStack),
				"%price%", format(currentPrice), "%currency%", this.economy.getCurrenry());

		if (this.buyCommands.size() != 0) {
			for (String command : this.buyCommands) {
				command = command.replace("%amount%", String.valueOf(amount));
				command = command.replace("%item%", getItemName(itemStack));
				command = command.replace("%price%", format(currentPrice));
				command = command.replace("%currency%", this.economy.getCurrenry());
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(command, player));
			}
		}

		if (this.log) {

			String logMessage = Message.BUY_LOG.getMessage();

			logMessage = logMessage.replace("%amount%", String.valueOf(amount));
			logMessage = logMessage.replace("%item%", getItemName(itemStack));
			logMessage = logMessage.replace("%price%", format(currentPrice));
			logMessage = logMessage.replace("%currency%", this.economy.getCurrenry());
			logMessage = logMessage.replace("%player%", player.getName());

			History history = new ZHistory(HistoryType.BUY, papi(logMessage, player));
			this.historyManager.asyncValue(player.getUniqueId(), history);

		}

	}

	@Override
	public void sell(Player player, int amount) {

		ItemStack itemStack = super.getItemStack().clone();
		int item = 0;

		for (int a = 0; a != 36; a++) {
			ItemStack is = player.getInventory().getContents()[a];
			if (is != null && is.isSimilar(itemStack)) {
				item += is.getAmount();
			}
		}

		if (item <= 0) {
			message(player, Message.NOT_ITEMS);
			return;
		}

		if (item < amount) {
			message(player, Message.NOT_ENOUGH_ITEMS);
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

		message(player, Message.SELL_ITEM, "%amount%", String.valueOf(realAmount), "%item%", getItemName(itemStack),
				"%price%", format(currentPrice), "%currency%", this.economy.getCurrenry());

		if (sellCommands.size() != 0)
			for (String command : sellCommands) {
				command = command.replace("%amount%", String.valueOf(amount));
				command = command.replace("%item%", getItemName(itemStack));
				command = command.replace("%price%", format(currentPrice));
				command = command.replace("%currency%", this.economy.getCurrenry());
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(command, player));
			}

		if (log) {

			String logMessage = Message.SELL_LOG.getMessage();

			logMessage = logMessage.replace("%amount%", String.valueOf(amount));
			logMessage = logMessage.replace("%item%", getItemName(itemStack));
			logMessage = logMessage.replace("%price%", format(currentPrice));
			logMessage = logMessage.replace("%currency%", this.economy.getCurrenry());
			logMessage = logMessage.replace("%player%", player.getName());

			History history = new ZHistory(HistoryType.SELL, logMessage);
			this.historyManager.asyncValue(player.getUniqueId(), history);

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
			if (!str.equals(string)) {
				str = str.replace("%currency%", this.canBuy() ? this.economy.getCurrenry() : "");
			}

			str = str.replace("%sellPrice%", this.getSellPriceAsString(optionalSell, 1));
			if (!str.equals(string)) {
				str = str.replace("%currency%", this.canSell() ? this.economy.getCurrenry() : "");
			}
			str = str.replace("&", "§");

			if (optionalBuy.isPresent() && this.canBuy()) {
				str = str.replace("%buyPermission%",
						Message.BUY_PERMISSION.replace("%percent%", format(optionalBuy.get().getPercent())));
			} else {
				str = str.replace("%buyPermission%", "");
			}

			if (optionalSell.isPresent() && this.canSell()) {
				str = str.replace("%sellPermission%",
						Message.SELL_PERMISSION.replace("%percent%", format(optionalSell.get().getPercent())));
			} else {
				str = str.replace("%sellPermission%", "");
			}

			if ((str.contains("%buyPermissionLine%") && (!optionalBuy.isPresent() || !this.canBuy()))
					|| (str.contains("%sellPermissionLine%") && (!optionalBuy.isPresent() || !this.canSell()))) {
				return;
			}

			if (str.contains("%buyPermissionLine%") && optionalBuy.isPresent() && this.canBuy()) {
				str = Message.BUY_PERMISSIONS_LINE.replace("%percent%", format(optionalSell.get().getPercent()));
			} else if (str.contains("%sellPermissionLine%") && optionalSell.isPresent() && this.canSell()) {
				str = Message.SELL_PERMISSION_LINE.replace("%percent%", format(optionalSell.get().getPercent()));
			}

			lore.add(str);
		});
		itemMeta.setLore(color(lore));
		itemStack.setItemMeta(itemMeta);
		return playerHead(papi(itemStack, player), player);
	}

	@Override
	public ItemStack getCustomItemStack(Player player) {
		return this.createItemStack(player);
	}

	@Override
	public String getSellPriceAsString(Player player, int amount) {
		return this.canSell() ? format(getSellPrice(player) * amount) : Message.CANT_SELL.getMessage();
	}

	@Override
	public String getBuyPriceAsString(Player player, int amount) {
		return this.canBuy() ? format(getBuyPrice(player) * amount) : Message.CANT_BUY.getMessage();
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
		return this.canSell() ? format(getSellPrice(optional) * amount) : Message.CANT_SELL.getMessage();
	}

	public String getBuyPriceAsString(Optional<Permission> optional, int amount) {
		return this.canBuy() ? format(getBuyPrice(optional) * amount) : Message.CANT_BUY.getMessage();
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
