package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.enums.Economy;

public interface ItemConfirmDoubleButton extends PlaceholderButton, PermissibleButton {

	/**
	 * 
	 * @return
	 */
	public long getRightPrice();

	/**
	 * 
	 * @return
	 */
	public Economy getRightEconomy();

	/**
	 * 
	 * @return
	 */
	public long getLeftPrice();

	/**
	 * 
	 * @return
	 */
	public Economy getLeftEconomy();

	/**
	 * 
	 * @return commands list
	 */
	public List<String> getRightCommands();

	/**
	 * 
	 * @return
	 */
	public List<String> getLeftCommands();

	/**
	 * 
	 * @param player
	 */
	public void rightSell(Player player);

	/**
	 * 
	 * @param player
	 */
	public void leftSell(Player player);

}
