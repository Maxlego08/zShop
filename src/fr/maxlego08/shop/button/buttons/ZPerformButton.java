package fr.maxlego08.shop.button.buttons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.PerformButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.PlaceholderAction;
import fr.maxlego08.shop.api.sound.SoundOption;

public class ZPerformButton extends ZPlaceholderButton implements PerformButton {

	private final List<String> commands;
	private final List<String> consoleCommands;
	private final List<String> consolePermissionCommands;
	private final String consolePermission;
	private final boolean closeInventory;

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
	 * @param commands
	 * @param closeInventory
	 */
	public ZPerformButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, String value,
			List<String> commands, List<String> consoleCommands, boolean closeInventory, boolean glow,
			SoundOption sound, List<String> consolePermissionCommands, String consolePermission) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value, glow,
				sound);
		this.commands = commands;
		this.consoleCommands = consoleCommands;
		this.closeInventory = closeInventory;
		this.consolePermissionCommands = consolePermissionCommands;
		this.consolePermission = consolePermission;
	}

	@Override
	public List<String> getCommands() {
		return commands;
	}

	@Override
	public void execute(Player player) {

		if (!checkPermission(player))
			return;

		if (this.closeInventory())
			player.closeInventory();
		papi(new ArrayList<String>(commands), player)
				.forEach(command -> player.performCommand(command.replace("%player%", player.getName())));

		papi(new ArrayList<String>(consoleCommands), player).forEach(command -> Bukkit
				.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));

		if (consolePermission != null && player.hasPermission(consolePermission)) {
			papi(new ArrayList<String>(consolePermissionCommands), player).forEach(command -> Bukkit
					.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
		}
	}

	@Override
	public boolean closeInventory() {
		return closeInventory;
	}

	@Override
	public List<String> getConsoleCommands() {
		return consoleCommands;
	}

	@Override
	public List<String> getConsolePermissionCommands() {
		return consolePermissionCommands;
	}

	@Override
	public String getConsolePermission() {
		return consolePermission;
	}

}
