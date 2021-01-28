package fr.maxlego08.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.IEconomy;
import fr.maxlego08.shop.api.enums.Economy;
import fr.maxlego08.shop.api.events.EconomyDepositEvent;
import fr.maxlego08.shop.api.events.EconomyMoneyEvent;
import fr.maxlego08.shop.api.events.EconomyWithdrawMoney;
import me.bukkit.mTokens.Inkzzz.Tokens;
import me.realized.tokenmanager.api.TokenManager;

public class ZEconomy implements IEconomy {

	private final ZShop plugin;

	private final TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");

	/**
	 * @param plugin
	 */
	public ZEconomy(ZShop plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public boolean hasMoney(Economy economy, Player player, double price) {
		return getMoney(economy, player) >= price;
	}

	@Override
	public void depositMoney(Economy economy, Player player, double value) {
		switch (economy) {
		case MYSQLTOKEN:
			Tokens.getInstance().getAPI().giveTokens(player, (int) value);
			break;
		case TOKENMANAGER:
			tokenManager.addTokens(player, (long) value);
			break;
		case PLAYERPOINT:
			plugin.getPlayerPointsAPI().give(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			plugin.getEconomy().depositPlayer(player, value);
			break;
		case CUSTOM:
			EconomyDepositEvent event = new EconomyDepositEvent(player, value);
			event.callEvent();
			break;
		case LEVEL:
			int level = player.getLevel();
			player.setLevel((int) (level + value));
			break;
		default:
			break;
		}
	}

	@Override
	public void withdrawMoney(Economy economy, Player player, double value) {
		switch (economy) {
		case MYSQLTOKEN:
			Tokens.getInstance().getAPI().takeTokens(player, (int) value);
			break;
		case TOKENMANAGER:
			tokenManager.removeTokens(player, (long) value);
			break;
		case PLAYERPOINT:
			plugin.getPlayerPointsAPI().take(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			plugin.getEconomy().withdrawPlayer(player, value);
			break;
		case CUSTOM:
			EconomyWithdrawMoney event = new EconomyWithdrawMoney(player, value);
			event.callEvent();
			break;
		case LEVEL:
			int level = player.getLevel();
			player.setLevel((int) (level - value));
			break;
		default:
			break;
		}
	}

	@Override
	public double getMoney(Economy economy, Player player) {
		switch (economy) {
		case MYSQLTOKEN:
			return Tokens.getInstance().getAPI().getTokens(player);
		case TOKENMANAGER:
			return tokenManager.getTokens(player).getAsLong();
		case PLAYERPOINT:
			return plugin.getPlayerPointsAPI().look(player.getUniqueId());
		case VAULT:
			return plugin.getEconomy().getBalance(player);
		case CUSTOM:
			EconomyMoneyEvent event = new EconomyMoneyEvent(player);
			event.callEvent();
			return event.getMoney();
		case LEVEL:
			return player.getLevel();
		default:
			return 0.0;
		}
	}

}
