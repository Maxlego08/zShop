package fr.maxlego08.zshop.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import fr.maxlego08.zshop.zcore.utils.plugins.Plugins;
import fr.maxlego08.ztranslator.api.Translator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TranslationHelper {

	/**
	 * Allows to translate the item name, if the zTranslator plugin is active, then the translated name will be retrieved
	 * 
	 * @param offlinePlayer
	 * @param itemStack
	 * @return item name
	 */
	protected String getItemName(OfflinePlayer offlinePlayer, ItemStack itemStack) {

		if (itemStack == null) {
			return "";

		}
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			return itemStack.getItemMeta().getDisplayName();
		}

		if (Bukkit.getPluginManager().isPluginEnabled(Plugins.ZTRANSLATOR.getName())) {

			RegisteredServiceProvider<Translator> provider = Bukkit.getServer().getServicesManager()
					.getRegistration(Translator.class);
			Translator translator = provider.getProvider();
			return translator.translate(offlinePlayer, itemStack);
		}
		
		String name = itemStack.serialize().get("type").toString().replace("_", " ").toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	/**
	 * Allows to translate the item name, if the zTranslator plugin is active, then the translated name will be retrieved
	 * 
	 * @param itemStack
	 * @return item name
	 */
	public String getItemName(ItemStack itemStack) {
		if (itemStack == null) return "";

		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			return convertOldHexString(itemStack.getItemMeta().getDisplayName());
		}

		if (Bukkit.getPluginManager().isPluginEnabled(Plugins.ZTRANSLATOR.getName())) {

			RegisteredServiceProvider<Translator> provider = Bukkit.getServer().getServicesManager()
					.getRegistration(Translator.class);
			Translator translator = provider.getProvider();
			return translator.translate(itemStack);
		}
		
		String name = itemStack.serialize().get("type").toString().replace("_", " ").toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private String convertOldHexString(String string) {

		if (string == null) return null;

		Pattern pattern = Pattern.compile("§x[a-fA-F0-9-§]{12}");
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			String color = string.substring(matcher.start(), matcher.end());
			String colorReplace = color.replace("§x", "#");
			colorReplace = colorReplace.replace("§", "");
			string = string.replace(color, colorReplace);
			matcher = pattern.matcher(string);
		}

		return string;
	}


}
