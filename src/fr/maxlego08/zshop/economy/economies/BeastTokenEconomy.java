package fr.maxlego08.zshop.economy.economies;

import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.OfflinePlayer;

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
        return offlinePlayer.isOnline() ? BeastTokensAPI.getTokensManager().getTokens(offlinePlayer.getPlayer()) : BeastTokensAPI.getTokensManager().getTokens(offlinePlayer);
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
		if (offlinePlayer.isOnline()) BeastTokensAPI.getTokensManager().addTokens(offlinePlayer.getPlayer(), value);
		else BeastTokensAPI.getTokensManager().addTokens(offlinePlayer, value);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
		if (offlinePlayer.isOnline()) BeastTokensAPI.getTokensManager().removeTokens(offlinePlayer.getPlayer(), value);
		else BeastTokensAPI.getTokensManager().removeTokens(offlinePlayer, value);
    }
}
