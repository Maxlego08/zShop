package fr.maxlego08.shop.api.inventory;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.PermissibleButton;
import fr.maxlego08.shop.api.enums.InventoryType;

public interface Inventory {

	/**
	 * 
	 * @return inventory size
	 */
	public int size();
	
	/**
	 * 
	 * @return
	 */
	public InventoryType getType();
	
	
	/**
	 * 
	 * @return inventory name
	 */
	public String getName();
	
	/**
	 * 
	 * @param replace
	 * @param newChar
	 * @return inventory name
	 */
	public String getName(String replace, String newChar);
	
	/**
	 * 
	 * @param button type
	 * @return buttons list
	 */
	public <T extends Button> List<T> getButtons(Class<T> type);
	
	/**
	 * 
	 * @return buttons list
	 */
	public List<Button> getButtons();
	
	/**
	 * 
	 * @param player
	 */
	public void open(Player player);

	/**
	 * 
	 * @param page
	 * @return
	 */
	public List<PermissibleButton> sortButtons(int page);
	
	/**
	 * 
	 * @return int
	 */
	public int getMaxPage();
	
	/**
	 * Allows to retrieve an itembutton
	 * @param itemStack
	 * @return item button
	 */
	public Optional<ItemButton> getItemButton(ItemStack itemStack);
	
	/**
	 * 
	 * @return
	 */
	public String getFileName();

	
}
