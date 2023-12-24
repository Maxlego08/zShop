package fr.maxlego08.zshop.economy.economies;

import org.bukkit.OfflinePlayer;

import me.mraxetv.beasttokens.api.BeastTokensAPI;

public class BeastTokenEconomy extends DefaultEconomy {

	/**
	 * @param name
	 * @param currency
	 * @param denyMessage
	 */
	public BeastTokenEconomy(String name, String currency, String denyMessage) {
		super(name, currency, denyMessage);
	}

	@Override
	public double getMoney(OfflinePlayer offlinePlayer) {
		return (long) BeastTokensAPI.getTokensManager().getTokens(offlinePlayer);
	}

	@Override
	public void depositMoney(OfflinePlayer offlinePlayer, double value) {
		BeastTokensAPI.getTokensManager().addTokens(offlinePlayer, value);
	}

	@Override
	public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
		BeastTokensAPI.getTokensManager().removeTokens(offlinePlayer, value);
	}
}
