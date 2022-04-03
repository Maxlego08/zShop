package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface PerformButton extends PermissibleButton {

	/**
	 * 
	 * @return commands list
	 */
	public List<String> getCommands();

	/**
	 * 
	 * @return console commands
	 */
	public List<String> getConsoleCommands();

	/**
	 * 
	 * @return
	 */
	public List<String> getConsolePermissionCommands();

	/**
	 * 
	 * @return
	 */
	public String getConsolePermission();

	/**
	 * 
	 * @param player
	 */
	void execute(Player player, ClickType type);

	/**
	 * 
	 * @return true if inventory need to be close
	 */
	@Deprecated
	public boolean closeInventory();

}
