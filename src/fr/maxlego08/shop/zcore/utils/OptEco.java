package fr.maxlego08.shop.zcore.utils;

import org.bukkit.OfflinePlayer;

public class OptEco {

	public OptEco() {
		// TODO Auto-generated constructor stub
	}

	public void depositMoney(OfflinePlayer player, double value) {
		me.playernguyen.opteco.OptEco plugin = me.playernguyen.opteco.OptEco.getInstance();
		me.playernguyen.opteco.api.OptEcoAPI api = new me.playernguyen.opteco.api.OptEcoAPIAbstract(plugin);
		api.addPoints(player.getUniqueId(), value);
	}

	public void withdrawMoney(OfflinePlayer player, double value) {
		me.playernguyen.opteco.OptEco plugin = me.playernguyen.opteco.OptEco.getInstance();
		me.playernguyen.opteco.api.OptEcoAPI api = new me.playernguyen.opteco.api.OptEcoAPIAbstract(plugin);
		api.takePoints(player.getUniqueId(), value);
	}

	public double getMoney(OfflinePlayer player) {
		me.playernguyen.opteco.OptEco plugin = me.playernguyen.opteco.OptEco.getInstance();
		me.playernguyen.opteco.api.OptEcoAPI api = new me.playernguyen.opteco.api.OptEcoAPIAbstract(plugin);
		return api.getPoints(player.getUniqueId());
	}

}
