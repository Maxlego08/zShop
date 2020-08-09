package fr.maxlego08.shop.api;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.api.command.Command;

public interface ShopManager {

	public void loadCommands() throws Exception;

	public void open(Player player, Command command);
	
}
