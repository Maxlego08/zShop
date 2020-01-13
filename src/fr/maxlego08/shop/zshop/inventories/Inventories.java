package fr.maxlego08.shop.zshop.inventories;

import org.bukkit.command.Command;

public interface Inventories {

	void load();

	void save();

	void saveDefault();

	InventoryObject getInventory(int id);

	InventoryObject getInventory(Command command);

}
