package fr.maxlego08.shop.zcore.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.enums.Message;
import fr.maxlego08.shop.zcore.utils.players.ActionBar;

public class MessageUtils {

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void message(CommandSender player, Message message) {
		player.sendMessage(Lang.prefix + " " + message.msg());
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void message(CommandSender player, String message, Object... args) {
		player.sendMessage(Lang.prefix + " " + String.format(message, args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void messageWO(CommandSender player, Message message) {
		player.sendMessage(message.msg());
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void messageWO(CommandSender player, Message message, Object... args) {
		player.sendMessage(String.format(message.msg(), args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void message(CommandSender player, Message message, Object... args) {
		player.sendMessage(Lang.prefix + " " + String.format(message.msg(), args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void actionMessage(Player player, Message message, Object... args) {
		ActionBar.sendActionBar(player, String.format(message.msg(), args));
	}

}
