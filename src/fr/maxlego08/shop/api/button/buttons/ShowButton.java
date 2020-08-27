package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.enums.InventoryType;

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
	public List<String> getLore(ItemButton button, int amount, InventoryType type);
	
	/**
	 * 
	 * @param button
	 * @return
	 */
	public ItemStack applyLore(ItemButton button, int amount, InventoryType type);
	
}
