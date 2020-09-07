package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;

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
	 * @param player
	 */
	void execute(Player player);
	
	/**
	 * 
	 * @return true if inventory need to be close
	 */
	public boolean closeInventory();
	
}
