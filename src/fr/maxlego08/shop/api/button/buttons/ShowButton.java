package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

import fr.maxlego08.shop.api.button.Button;

public interface ShowButton extends Button {

	/**
	 * Get item lore
	 * @return lore
	 */
	public List<String> getLore();

	/**
	 * Get item lore with button
	 * @param button
	 * @return lore
	 */
	public List<String> getLore(ItemButton button);
	
}
