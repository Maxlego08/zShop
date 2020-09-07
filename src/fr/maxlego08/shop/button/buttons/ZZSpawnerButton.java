package fr.maxlego08.shop.button.buttons;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ZSpawnerButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.enums.ZSpawnerAction;
import fr.maxlego08.shop.api.events.ZShopBuyEvent;
import fr.maxlego08.shop.api.history.History;
import fr.maxlego08.shop.api.history.HistoryType;
import fr.maxlego08.shop.history.ZHistory;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.zspawner.api.manager.SpawnerManager;

public class ZZSpawnerButton extends ZItemButton implements ZSpawnerButton {

	private final EntityType type;
	private final ZSpawnerAction action;
	private final SpawnerManager manager;
	private final int level;

	/**
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
	 * @param type2
	 * @param action2
	 */
	public ZZSpawnerButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, double value,
			ShopManager manager, IEconomy iEconomy, double sellPrice, double buyPrice, int maxStack, Economy economy,
			List<String> lore, List<String> buyCommands, List<String> sellCommands, boolean giveItem, EntityType entity,
			ZSpawnerAction zAction, Plugin plugin, int level, boolean glow, boolean log) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value, manager,
				iEconomy, sellPrice, buyPrice, maxStack, economy, lore, buyCommands, sellCommands, giveItem, glow, log);
		this.type = entity;
		this.action = zAction;
		this.manager = getProvider(plugin, SpawnerManager.class);
		this.level = level;
		if (this.manager == null)
			Logger.info("Impossible to find the zSpawner plugin !", LogType.ERROR);
	}

	@Override
	public EntityType getEntityType() {
		return type;
	}

	@Override
	public ZSpawnerAction getZSpawnerAction() {
		return action;
	}
	
	@Override
	public boolean needToConfirm() {
		return true;
	}
	
	@Override
	public void buy(Player player, int amount) {

		double currentPrice = this.getBuyPrice(player) * amount;

		if (currentPrice < 0)
			return;

		if (!super.getIEconomy().hasMoney(getEconomy(), player, currentPrice)) {
			message(player, this.getEconomy().getDenyMessage());
			return;
		}

		if (hasInventoryFull(player)) {
			message(player, Lang.notEnouhtPlace);
			return;
		}

		ZShopBuyEvent event = new ZShopBuyEvent(this, player, amount, currentPrice, super.getEconomy());
		event.callEvent();

		if (event.isCancelled())
			return;

		Economy economy = event.getEconomy();
		currentPrice = event.getPrice();
		amount = event.getAmount();

		super.getIEconomy().withdrawMoney(economy, player, currentPrice);

		ItemStack itemStack = super.getItemStack().clone();
		itemStack.setAmount(amount);
		
		switch (this.action) {
		case ADD:
			manager.addSpawner(Bukkit.getConsoleSender(), player, type, amount, level);
			break;
		case GIVE:
			manager.giveSpawner(Bukkit.getConsoleSender(), player, type, amount, level);
			break;
		default:
			break;
		}

		String message = Lang.buyItem;
		message = message.replace("%amount%", String.valueOf(amount));
		message = message.replace("%item%", getItemName(itemStack));
		message = message.replace("%price%", format(currentPrice));
		message = message.replace("%currency%", super.getEconomy().getCurrenry());

		message(player, message);

		if (super.getBuyCommands().size() != 0)
			for (String command : super.getBuyCommands()) {
				command = command.replace("%amount%", String.valueOf(amount));
				command = command.replace("%item%", getItemName(itemStack));
				command = command.replace("%price%", format(currentPrice));
				command = command.replace("%currency%", super.getEconomy().getCurrenry());
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(command, player));
			}

		if (super.log){
			
			String logMessage = Lang.buyLog;
			
			logMessage = logMessage.replace("%amount%", String.valueOf(amount));
			logMessage = logMessage.replace("%item%", getItemName(itemStack));
			logMessage = logMessage.replace("%price%", format(currentPrice));
			logMessage = logMessage.replace("%currency%", this.getEconomy().getCurrenry());
			logMessage = logMessage.replace("%player%", player.getName());
			
			History history = new ZHistory(HistoryType.BUY, logMessage);
			this.historyManager.asyncValue(player.getUniqueId(), history);
			
		}
		
	}
	

}
