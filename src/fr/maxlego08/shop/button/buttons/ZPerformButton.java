package fr.maxlego08.shop.button.buttons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.PerformButton;
import fr.maxlego08.shop.api.enums.ButtonType;

public class ZPerformButton extends ZPermissibleButton implements PerformButton {

	private final List<String> commands;
	private final boolean closeInventory;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param isPermanent
	 * @param commands
	 * @param closeInventory
	 */
	public ZPerformButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, List<String> commands, boolean closeInventory) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent);
		this.commands = commands;
		this.closeInventory = closeInventory;
	}

	@Override
	public List<String> getCommands() {
		return commands;
	}

	@Override
	public void execute(Player player) {
		if (this.closeInventory())
			player.closeInventory();
		papi(new ArrayList<String>(commands), player).forEach(command -> player.performCommand(command.replace("%player%", player.getName())));
	}

	@Override
	public boolean closeInventory() {
		return closeInventory;
	}

}
