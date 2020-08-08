package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

public interface CommandButton extends ItemButton{

	/**
	 * 
	 * @return commands list
	 */
	public List<String> getCommands();
	
	/**
	 * 
	 * @return commands amount
	 */
	public int count();
	
}
