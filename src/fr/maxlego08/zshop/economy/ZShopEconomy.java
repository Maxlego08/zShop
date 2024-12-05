package fr.maxlego08.zshop.economy;

import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.zcore.utils.ZUtils;
import fr.traqueur.currencies.CurrencyProvider;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;

public class ZShopEconomy extends ZUtils implements ShopEconomy {

    private final String name;
    private final String currency;
    private final String denyMessage;
    private final CurrencyProvider currencyProvider;

    public ZShopEconomy(String name, String currency, String denyMessage, CurrencyProvider currencyProvider) {
        this.name = name;
        this.currency = currency;
        this.denyMessage = denyMessage;
        this.currencyProvider = currencyProvider;
    }

    @Override
    public String getCurrency() {
        return this.currency;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDenyMessage() {
        return this.denyMessage;
    }

    public boolean hasMoney(OfflinePlayer offlinePlayer, double price) {
        return getMoney(offlinePlayer) >= price;
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        return this.currencyProvider.getBalance(offlinePlayer).doubleValue();
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value, String reason) {
        this.currencyProvider.deposit(offlinePlayer, BigDecimal.valueOf(value), reason);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value, String reason) {
        this.currencyProvider.withdraw(offlinePlayer, BigDecimal.valueOf(value), reason);
    }

    public CurrencyProvider getCurrencyProvider() {
        return currencyProvider;
    }
}
