package fr.maxlego08.shop.zshop.items;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.utils.ZUtils;

public class EconomyUtils extends ZUtils {

	/**
	 * @param economy
	 * @param player
	 * @param price
	 * @return
	 */
	protected boolean hasMoney(Economy economy, Player player, double price) {
		switch (economy) {
		case PLAYERPOINT:
			return ZPlugin.z().getPlayerPointsAPI().look(player.getUniqueId()) >= (int)price;
		case VAULT:
			return super.getBalance(player) >= price;
		default:
			return false;
		}
	}

	/**
	 * @param economy
	 * @param player
	 * @param value
	 */
	protected void depositMoney(Economy economy, Player player, double value) {
		switch (economy) {
		case PLAYERPOINT:
			ZPlugin.z().getPlayerPointsAPI().give(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			super.depositMoney(player, value);
			break;
		default:
			break;
		}
	}
	
	/**
	 * @param economy
	 * @param player
	 * @param value
	 */
	protected void withdrawMoney(Economy economy, Player player, double value) {
		switch (economy) {
		case PLAYERPOINT:
			ZPlugin.z().getPlayerPointsAPI().take(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			super.withdrawMoney(player, value);
			break;
		default:
			break;
		}
	}

}
