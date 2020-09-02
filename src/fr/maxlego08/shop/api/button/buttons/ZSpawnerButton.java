package fr.maxlego08.shop.api.button.buttons;

import org.bukkit.entity.EntityType;

import fr.maxlego08.shop.api.enums.ZSpawnerAction;

public interface ZSpawnerButton extends ItemButton {

	/**
	 * 
	 * @return
	 */
	public EntityType getEntityType();
	
	/**
	 * 
	 * @return
	 */
	public ZSpawnerAction getZSpawnerAction();
	
}
