package fr.maxlego08.shop.zshop.items;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import me.bukkit.mTokens.Inkzzz.Tokens;
import me.realized.tokenmanager.api.TokenManager;

public class EconomyUtils extends ZUtils {

	private TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");


	/**
	 * @param economy
	 * @param player
	 * @param price
	 * @return
	 */
	protected boolean hasMoney(Economy economy, Player player, double price) {
		switch (economy) {
		case MYSQLTOKEN:
			return Tokens.getInstance().getAPI().getTokens(player) >= price;
		case TOKENMANAGER:
			return tokenManager.getTokens(player).getAsLong() >= price;
		case PLAYERPOINT:
			return ZPlugin.z().getPlayerPointsAPI().look(player.getUniqueId()) >= (int) price;
		case VAULT:
			return super.getBalance(player) >= price;
		default:
			return false;
		}
	}

	/**
	 * @param economy
	 * @param player
	 * @param value
	 */
	protected void depositMoney(Economy economy, Player player, double value) {
		switch (economy) {
		case MYSQLTOKEN:
			Tokens.getInstance().getAPI().giveTokens(player, (int) value);
			break;
		case TOKENMANAGER:
			tokenManager.addTokens(player, (long) value);
			break;
		case PLAYERPOINT:
			ZPlugin.z().getPlayerPointsAPI().give(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			super.depositMoney(player, value);
			break;
		default:
			break;
		}
	}

	/**
	 * @param economy
	 * @param player
	 * @param value
	 */
	protected void withdrawMoney(Economy economy, Player player, double value) {
		switch (economy) {
		case MYSQLTOKEN:
			Tokens.getInstance().getAPI().takeTokens(player, (int) value);
			break;
		case TOKENMANAGER:
			tokenManager.removeTokens(player, (long) value);
			break;
		case PLAYERPOINT:
			ZPlugin.z().getPlayerPointsAPI().take(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			super.withdrawMoney(player, value);
			break;
		default:
			break;
		}
	}

}
