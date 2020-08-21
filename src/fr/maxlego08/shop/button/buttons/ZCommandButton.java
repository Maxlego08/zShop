package fr.maxlego08.shop.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.CommandButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;

public class ZCommandButton extends ZItemButton implements CommandButton {

	private final List<String> commands;

	/**
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
	 * @param commands
	 */
	public ZCommandButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, IEconomy iEconomy, double sellPrice, double buyPrice, Economy economy,
			List<String> commands, int maxStack, List<String> lore) {
		super(type, itemStack, slot, permission, message, elseButton, iEconomy, sellPrice, buyPrice, economy, maxStack, lore);
		this.commands = commands;
	}

	@Override
	public List<String> getCommands() {
		return commands;
	}

	@Override
	public int count() {
		return commands.size();
	}

	@Override
	public void buy(Player player, int amount) {

	}

	@Override
	public void sell(Player player, int amount) {

	}

}
