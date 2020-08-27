package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

public interface SlotButton extends PermissibleButton {

	/**
	 * 
	 * @return
	 */
	public List<Integer> getSlots();
	
}
