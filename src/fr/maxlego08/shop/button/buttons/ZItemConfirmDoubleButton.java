package fr.maxlego08.shop.button.buttons;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ItemConfirmDoubleButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.sound.SoundOption;
import fr.maxlego08.shop.save.Lang;

public class ZItemConfirmDoubleButton extends ZPlaceholderButton implements ItemConfirmDoubleButton {

	private final IEconomy iEconomy;
//	private final ShopManager manager;
	private final long rightPrice;
	private final Economy rightEconomy;
	private final long leftPrice;
	private final Economy leftEconomy;
	private final List<String> rightCommands;
	private final List<String> leftCommands;

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
	 * @param needGlow
	 * @param sound
	 * @param rightPrice
	 * @param rightEconomy
	 * @param leftPrice
	 * @param leftEconomy
	 * @param rightCommands
	 * @param leftCommands
	 * @param lore
	 */
	public ZItemConfirmDoubleButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, String value,
			boolean needGlow, SoundOption sound, long rightPrice, Economy rightEconomy, long leftPrice,
			Economy leftEconomy, List<String> rightCommands, List<String> leftCommands, IEconomy iEconomy,
			ShopManager manager) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value, needGlow,
				sound);
		this.rightPrice = rightPrice;
		this.rightEconomy = rightEconomy;
		this.leftPrice = leftPrice;
		this.leftEconomy = leftEconomy;
		this.rightCommands = rightCommands;
		this.leftCommands = leftCommands;
//		this.manager = manager;
		this.iEconomy = iEconomy;
	}

	@Override
	public long getRightPrice() {
		return this.rightPrice;
	}

	@Override
	public Economy getRightEconomy() {
		return this.rightEconomy;
	}

	@Override
	public long getLeftPrice() {
		return this.leftPrice;
	}

	@Override
	public Economy getLeftEconomy() {
		return this.leftEconomy;
	}

	@Override
	public List<String> getRightCommands() {
		return this.rightCommands;
	}

	@Override
	public List<String> getLeftCommands() {
		return this.leftCommands;
	}

	@Override
	public void rightSell(Player player) {

		Economy economy = this.rightEconomy;
		long currentPrice = this.rightPrice;
		int amount = 1;

		if (!iEconomy.hasMoney(economy, player, currentPrice)) {
			message(player, economy.getDenyMessage());
			return;
		}

		iEconomy.withdrawMoney(economy, player, currentPrice);

		String message = Lang.buyItem;
		message = message.replace("%amount%", String.valueOf(amount));
		message = message.replace("%item%", getItemName(this.getItemStack()));
		message = message.replace("%price%", format(currentPrice));
		message = message.replace("%currency%", economy.getCurrenry());

		message(player, message);

		for (String command : this.rightCommands) {
			command = command.replace("%amount%", String.valueOf(amount));
			command = command.replace("%item%", getItemName(this.getItemStack()));
			command = command.replace("%price%", format(currentPrice));
			command = command.replace("%currency%", economy.getCurrenry());
			command = command.replace("%player%", player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(command, player));
		}

	}

	@Override
	public void leftSell(Player player) {

		Economy economy = this.leftEconomy;
		long currentPrice = this.leftPrice;
		int amount = 1;

		if (!iEconomy.hasMoney(economy, player, currentPrice)) {
			message(player, economy.getDenyMessage());
			return;
		}

		iEconomy.withdrawMoney(economy, player, currentPrice);

		String message = Lang.buyItem;
		message = message.replace("%amount%", String.valueOf(amount));
		message = message.replace("%item%", getItemName(this.getItemStack()));
		message = message.replace("%price%", format(currentPrice));
		message = message.replace("%currency%", economy.getCurrenry());

		message(player, message);

		for (String command : this.leftCommands) {
			command = command.replace("%amount%", String.valueOf(amount));
			command = command.replace("%item%", getItemName(this.getItemStack()));
			command = command.replace("%price%", format(currentPrice));
			command = command.replace("%currency%", economy.getCurrenry());
			command = command.replace("%player%", player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(command, player));
		}

	}

}
