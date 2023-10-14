package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.zshop.zcore.utils.plugins.Plugins;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class PlayerPointEconomy extends DefaultExample {

    private final PlayerPointsAPI playerPointsApi;

    public PlayerPointEconomy(Plugin plugin, String name, String currency, String format, String denyMessage) {
        super(name, currency, format, denyMessage);
        PlayerPoints playerPoints = (PlayerPoints) plugin.getServer().getPluginManager().getPlugin(Plugins.PLAYERPOINT.getName());
        this.playerPointsApi = playerPoints.getAPI();
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        return this.playerPointsApi.look(offlinePlayer.getUniqueId());
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        this.playerPointsApi.give(offlinePlayer.getUniqueId(), (int) value);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        this.playerPointsApi.take(offlinePlayer.getUniqueId(), (int) value);
    }
}