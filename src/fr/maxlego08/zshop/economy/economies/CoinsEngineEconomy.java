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
        return CoinsEngineAPI.getBalance(offlinePlayer.getPlayer(), currency);
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        CoinsEngineAPI.addBalance(offlinePlayer.getPlayer(), currency, value);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        CoinsEngineAPI.removeBalance(offlinePlayer.getPlayer(), currency, value);
    }
}
