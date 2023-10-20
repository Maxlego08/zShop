package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.zshop.api.economy.ShopEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomy extends DefaultExample {

    private final Economy economy;

    public VaultEconomy(Plugin plugin, String name, String currency, String denyMessage) {
        super(name, currency, denyMessage);
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
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        this.economy.depositPlayer(offlinePlayer, value);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        this.economy.withdrawPlayer(offlinePlayer, value);
    }
}
