package fr.maxlego08.zshop.economy.economies;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public class CoinsEngineEconomy extends DefaultEconomy {

    private final Currency currency;

    public CoinsEngineEconomy(Plugin plugin, String name, String currency, String denyMessage, String currencyName) {
        super(name, currency, denyMessage);
        this.currency = CoinsEngineAPI.getCurrency(currencyName);
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        return CoinsEngineAPI.getUserData(offlinePlayer.getUniqueId()).getCurrencyData(this.currency).getBalance();
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        CoinsEngineAPI.getUserData(offlinePlayer.getUniqueId()).removeBalance(this.currency, value);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        CoinsEngineAPI.getUserData(offlinePlayer.getUniqueId()).addBalance(this.currency, value);
    }
}
