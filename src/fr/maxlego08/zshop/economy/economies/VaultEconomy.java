package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.zshop.api.economy.ShopEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomy implements ShopEconomy {

    private final String name;
    private final String currency;
    private final String format;
    private final Economy economy;

    public VaultEconomy(Plugin plugin, String name, String currency, String format) {
        this.name = name;
        this.currency = currency;
        this.format = format;
        RegisteredServiceProvider<Economy> economyRegisteredServiceProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyRegisteredServiceProvider == null) {
            throw new NullPointerException("Vault Economy interface not found");
        }
        economy = economyRegisteredServiceProvider.getProvider();
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        return this.economy.getBalance(offlinePlayer);
    }

    @Override
    public boolean hasMoney(OfflinePlayer offlinePlayer, double price) {
        return getMoney(offlinePlayer) >= price;
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        this.economy.depositPlayer(offlinePlayer, value);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        this.economy.withdrawPlayer(offlinePlayer, value);
    }

    @Override
    public String getCurrency() {
        return this.currency;
    }

    @Override
    public String getFormat() {
        return this.format;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
