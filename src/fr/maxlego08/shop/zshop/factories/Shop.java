package fr.maxlego08.shop.zshop.factories;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.command.CommandType;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.utils.EnumCategory;
import net.citizensnpcs.api.npc.NPC;

public interface Shop {

	/**
	 * Open the shop
	 * 
	 * @param player
	 * @param category
	 * @param page
	 * @param args
	 */
	void openShop(Player player, EnumCategory category, int page, int id, String permission, Object... args);

	/**
	 * Open shop with citizen 
	 * @param player
	 * @param npc
	 */
	void openShopWithCitizen(Player player, NPC npc);
	
	/**
	 * @param player
	 * @param category
	 * @param page
	 * @param permission
	 * @param args
	 */
	void openShop(Player player, EnumCategory category, int page, int id, Permission permission, Object... args);
	
	/**
	 * @param player
	 * @param command
	 */
	void openShop(Player player, Command command);

	/**
	 * Open shop with category
	 * 
	 * @param player
	 * @param category
	 */
	CommandType openShop(Player player, String category);
	
	/**
	 * Open shop with category
	 * 
	 * @param player
	 * @param category
	 */
	CommandType openConfigShop(Player player, String category);

	/**
	 * Set item in hand
	 * 
	 * @param player
	 * @param amount
	 */
	void sellHand(Player player, int amount);

	/**
	 * Sells all items inventory which are the same as the one being held in
	 * your hand
	 * 
	 * @param player
	 */
	void sellAllHand(Player player);

	/**
	 * Sells all items
	 * 
	 * @param player
	 */
	void sellAll(Player player);

}
