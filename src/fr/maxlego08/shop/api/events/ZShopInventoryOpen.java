package fr.maxlego08.shop.api.events;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.command.Command;
import fr.maxlego08.shop.api.inventory.Inventory;
import fr.maxlego08.shop.zcore.utils.event.CancelledShopEvent;

public class ZShopInventoryOpen extends CancelledShopEvent {

	private final Inventory inventory;
	private final Command command;
	private final Player player;

	/**
	 * @param inventory
	 * @param command
	 * @param player
	 */
	public ZShopInventoryOpen(Inventory inventory, Command command, Player player) {
		super();
		this.inventory = inventory;
		this.command = command;
		this.player = player;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the command
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

}
