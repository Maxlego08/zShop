package fr.maxlego08.shop.api.button.buttons;

import fr.maxlego08.shop.api.button.Button;

public interface PermissibleButton extends Button{

	/**
	 * 
	 * @return else button
	 */
	public Button getElseButton();
	
	/**
	 * 
	 * @return permission
	 */
	public String getPermission();
	
	/**
	 * 
	 * @return message
	 */
	public String getMessage();
	
	/**
	 * 
	 * @return true if permission is not null
	 */
	public boolean hasPermission();
	
	/**
	 * 
	 * @return true if else button is not null
	 */
	public boolean hasElseButton();
	
	/**
	 * 
	 * @return true if message is not null
	 */
	public boolean hasMessage();
	
}
