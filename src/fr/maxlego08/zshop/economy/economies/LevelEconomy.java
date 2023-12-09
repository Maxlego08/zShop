package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.zshop.zcore.logger.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class LevelEconomy extends DefaultEconomy {

    public LevelEconomy(String name, String currency, String denyMessage) {
        super(name, currency, denyMessage);
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            return player == null ? 0.0 : player.getLevel();
        } else return 0.0;
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player == null) {
                Logger.info("Deposit level to " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
                return;
            }
            int level = player.getLevel();
            player.setLevel((int) (level + value));
        } else Logger.info("Deposit level to " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player == null) {
                Logger.info("Withdraw level to " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
                return;
            }
            int level = player.getLevel();
            player.setLevel((int) (level - value));
        } else Logger.info("Withdraw level from " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
    }
}
