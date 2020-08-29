package fr.maxlego08.shop.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.enums.Message;
import me.clip.placeholderapi.PlaceholderAPI;

public class MessageUtils extends PapiUtils{

	private static boolean usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void message(CommandSender player, Message message) {
		String m = Lang.prefix + " " + message.msg();

		if (usePlaceHolder && player instanceof Player)
			m = PlaceholderAPI.setPlaceholders((Player) player, m);

		player.sendMessage(m);
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void message(CommandSender player, String message, Object... args) {

		String m = Lang.prefix + " " + message;
		if (args.length > 0)
			m = Lang.prefix + " " + String.format(message, args);

		if (usePlaceHolder && player instanceof OfflinePlayer)
			m = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, m);
		player.sendMessage(m);
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void messageWO(CommandSender player, Message message) {
		String m = message.msg();

		if (usePlaceHolder && player instanceof Player)
			m = PlaceholderAPI.setPlaceholders((Player) player, m);

		player.sendMessage(m);
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void messageWO(CommandSender player, Message message, Object... args) {
		String m = String.format(message.msg(), args);

		if (usePlaceHolder && player instanceof Player)
			m = PlaceholderAPI.setPlaceholders((Player) player, m);

		player.sendMessage(m);
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void message(CommandSender player, Message message, Object... args) {
		String m = Lang.prefix + " " + String.format(message.msg(), args);

		if (usePlaceHolder && player instanceof Player)
			m = PlaceholderAPI.setPlaceholders((Player) player, m);

		player.sendMessage(m);
	}

}
