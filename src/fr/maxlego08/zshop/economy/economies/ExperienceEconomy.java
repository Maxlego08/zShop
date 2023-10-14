package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.zshop.zcore.logger.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ExperienceEconomy extends DefaultExample {

    public ExperienceEconomy(String name, String currency, String format, String denyMessage) {
        super(name, currency, format, denyMessage);
    }

    public void setTotalExperience(Player player, int experience) {
        if (experience < 0) throw new IllegalArgumentException("Experience is negative!");
        player.setExp(0.0F);
        player.setLevel(0);
        player.setTotalExperience(0);
        int currentExperience = experience;
        while (currentExperience > 0) {
            int j = getExpAtLevel(player);
            currentExperience -= j;
            if (currentExperience >= 0) {
                player.giveExp(j);
                continue;
            }
            currentExperience += j;
            player.giveExp(currentExperience);
            currentExperience = 0;
        }
    }

    private int getExpAtLevel(Player player) {
        return getExpAtLevel(player.getLevel());
    }

    public int getExpAtLevel(int experience) {
        if (experience <= 15) return 2 * experience + 7;
        if (experience <= 30) return 5 * experience - 38;
        return 9 * experience - 158;
    }

    public int getTotalExperience(Player player) {
        int experience = Math.round(getExpAtLevel(player) * player.getExp());
        int playerLevel = player.getLevel();
        while (playerLevel > 0) {
            playerLevel--;
            experience += getExpAtLevel(playerLevel);
        }
        if (experience < 0) experience = Integer.MAX_VALUE;
        return experience;
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            return player == null ? 0.0 : getTotalExperience(player);
        } else return 0.0;
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player == null) {
                Logger.info("Deposit experience to " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
                return;
            }
            setTotalExperience(player, getTotalExperience(player) + (int) value);
        } else
            Logger.info("Deposit experience to " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player == null) {
                Logger.info("Withdraw experience to " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
                return;
            }
            int totalExperience = getTotalExperience(player);
            setTotalExperience(player, totalExperience < value ? 0 : totalExperience - (int) value);
        } else Logger.info("Withdraw level from " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
    }
}
