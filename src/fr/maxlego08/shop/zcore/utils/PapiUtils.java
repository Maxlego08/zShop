package fr.maxlego08.shop.zcore.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPI;

public class PapiUtils {

	private static boolean usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

	/**
	 * 
	 * @param itemStack
	 * @param player
	 * @return
	 */
	protected ItemStack papi(ItemStack itemStack, Player player) {

		if (!usePlaceHolder)
			return itemStack;

		ItemMeta itemMeta = itemStack.getItemMeta();

		if (itemMeta.hasDisplayName())
			itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, itemMeta.getDisplayName()));

		if (itemMeta.hasLore())
			itemMeta.setLore(PlaceholderAPI.setPlaceholders(player, itemMeta.getLore()));

		itemStack.setItemMeta(itemMeta);
		return itemStack;

	}

	/**
	 * 
	 * @param text
	 * @param player
	 * @return
	 */
	protected String papi(String text, Player player) {
		if (!usePlaceHolder)
			return text;
		return PlaceholderAPI.setPlaceholders(player, text);
	}
	
	/**
	 * 
	 * @param text
	 * @param player
	 * @return
	 */
	protected List<String> papi(List<String> text, Player player) {
		if (!usePlaceHolder)
			return text;
		return PlaceholderAPI.setPlaceholders(player, text);
	}

}
