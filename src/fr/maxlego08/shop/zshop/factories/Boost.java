package fr.maxlego08.shop.zshop.factories;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.storage.Saveable;
import fr.maxlego08.shop.zshop.boost.BoostItem;
import fr.maxlego08.shop.zshop.boost.BoostType;

public interface Boost extends Saveable{

	/**
	 * @return boost items
	 */
	Map<String, BoostItem> getBoosts();
	
	/**
	 * @param player
	 * @param material
	 * @param type
	 * @param modifier
	 * @param ms
	 */
	void boost(CommandSender sender, MaterialData material, BoostType type, double modifier, long ms);
	
	/**
	 * 
	 * @param sender
	 * @param data
	 */
	void stopBoost(CommandSender sender, MaterialData data);
	
	/**
	 * 
	 */
	void updateBoost();
	
	/**
	 * 
	 * @param itemStack
	 * @return
	 */
	boolean isBoost(ItemStack itemStack);
	
	/**
	 * 
	 * @param itemStack
	 * @return
	 */
	BoostItem getBoost(ItemStack itemStack);
	
	/**
	 * @param sender
	 */
	void show(CommandSender sender);
	
}
