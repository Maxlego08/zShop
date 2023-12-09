package fr.maxlego08.zshop.economy.economies;

import com.bencodez.votingplugin.VotingPluginHooks;
import com.bencodez.votingplugin.user.UserManager;
import org.bukkit.OfflinePlayer;

public class VotingEconomy extends DefaultEconomy {

    private final UserManager userManager = VotingPluginHooks.getInstance().getUserManager();

    public VotingEconomy(String name, String currency, String denyMessage) {
        super(name, currency, denyMessage);
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        return this.userManager.getVotingPluginUser(offlinePlayer.getUniqueId()).getPoints();
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        this.userManager.getVotingPluginUser(offlinePlayer.getUniqueId()).addPoints((int) value);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        this.userManager.getVotingPluginUser(offlinePlayer.getUniqueId()).removePoints((int) value);
    }
}
