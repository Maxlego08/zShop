package fr.maxlego08.shop.api.button.buttons;

import fr.maxlego08.shop.api.enums.PlaceholderAction;

public interface PlaceholderButton extends PermissibleButton{

	public String getPlaceHolder();

	public PlaceholderAction getAction();
	
	public boolean hasPlaceHolder();
	
	public String getValue();

}
