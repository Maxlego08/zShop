package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.zshop.zcore.logger.Logger;
import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class TokenManagerEconomy extends DefaultExample {

    private final TokenManager tokenManager;

    public TokenManagerEconomy(Plugin plugin, String name, String currency, String denyMessage) {
        super(name, currency, denyMessage);
        this.tokenManager = (TokenManager) plugin.getServer().getPluginManager().getPlugin("TokenManager");
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        return offlinePlayer.isOnline() ? this.tokenManager.getTokens(offlinePlayer.getPlayer()).getAsLong() : 0;
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            this.tokenManager.addTokens(offlinePlayer.getPlayer(), (int) value);
        } else {
            Logger.info("Cannot give money to the player \"" + offlinePlayer.getName() + "\", is offline. (TokenManager, price:" + value + ")");
        }
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            this.tokenManager.removeTokens(offlinePlayer.getPlayer(), (int) value);
        } else {
            Logger.info("Cannot give money to the player \"" + offlinePlayer.getName() + "\", is offline. (TokenManager, price:" + value + ")");
        }
    }
}
