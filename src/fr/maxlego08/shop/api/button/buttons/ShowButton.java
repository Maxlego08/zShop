package fr.maxlego08.shop.api.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;
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
	public List<String> getLore(Player player, ItemButton button, int amount, InventoryType type);
	
	/**
	 * 
	 * @param button
	 * @return
	 */
	public ItemStack applyLore(Player player, ItemButton button, int amount, InventoryType type);
	
}
