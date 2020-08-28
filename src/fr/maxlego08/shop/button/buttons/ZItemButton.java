package fr.maxlego08.shop.button.buttons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.api.events.ZShopBuyEvent;
import fr.maxlego08.shop.api.events.ZShopSellEvent;
import fr.maxlego08.shop.save.Lang;

public class ZItemButton extends ZPermissibleButton implements ItemButton {

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
	 * @param iEconomy
	 * @param sellPrice
	 * @param buyPrice
	 * @param economy
	 * @param maxStack
	 * @param lore
	 * @param isPermanent
	 * @param buyCommands
	 * @param sellCommands
	 * @param giveItem
	 */
	public ZItemButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, IEconomy iEconomy, double sellPrice, double buyPrice, Economy economy, int maxStack,
			List<String> lore, boolean isPermanent, List<String> buyCommands, List<String> sellCommands,
			boolean giveItem) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent);
		this.iEconomy = iEconomy;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.economy = economy;
		this.maxStack = maxStack;
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

		double currentPrice = this.buyPrice * amount;

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
			for(String command : buyCommands){
				command = command.replace("%amount%", String.valueOf(amount));
				command = command.replace("%item%", getItemName(itemStack));
				command = command.replace("%price%", format(currentPrice));
				command = command.replace("%currency%", this.economy.getCurrenry());
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
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

		double currentPrice = this.sellPrice * realAmount;

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
			for(String command : sellCommands){
				command = command.replace("%amount%", String.valueOf(amount));
				command = command.replace("%item%", getItemName(itemStack));
				command = command.replace("%price%", format(currentPrice));
				command = command.replace("%currency%", this.economy.getCurrenry());
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
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
	public ItemStack createItemStack() {

		ItemStack itemStack = super.getItemStack().clone();
		ItemMeta itemMeta = itemStack.getItemMeta();

		if (itemMeta == null)
			return itemStack;

		List<String> lore = new ArrayList<String>();

		if (itemMeta.hasLore())
			lore.addAll(itemMeta.getLore());

		this.lore.forEach(string -> {

			String str = string.replace("%buyPrice%", this.getBuyPriceAsString(1));
			if (!str.equals(string))
				str = str.replace("%currency%", this.canBuy() ? this.economy.getCurrenry() : "");

			str = str.replace("%sellPrice%", this.getSellPriceAsString(1));
			if (!str.equals(string))
				str = str.replace("%currency%", this.canSell() ? this.economy.getCurrenry() : "");
			str = str.replace("&", "§");

			lore.add(str);
		});
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	@Override
	public ItemStack getCustomItemStack() {
		return this.createItemStack();
	}

	@Override
	public String getSellPriceAsString(int amount) {
		return this.canSell() ? String.valueOf(sellPrice * amount) : Lang.canSell;
	}

	@Override
	public String getBuyPriceAsString(int amount) {
		return this.canBuy() ? String.valueOf(buyPrice * amount) : Lang.canBuy;
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

}
