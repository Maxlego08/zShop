package fr.maxlego08.shop.zshop.utils;

import java.util.List;

public class Command {

	private final String name;
	private final List<String> aliases;
	private final int inventoryId;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the aliases
	 */
	public List<String> getAliases() {
		return aliases;
	}

	/**
	 * @return the inventoryId
	 */
	public int getInventoryId() {
		return inventoryId;
	}

	public Command(String name, List<String> aliases, int inventoryId) {
		super();
		this.name = name;
		this.aliases = aliases;
		this.inventoryId = inventoryId;
	}

	public boolean match(org.bukkit.command.Command command, int id) {
		return (command.getName().equalsIgnoreCase(name) || contains(command.getAliases(), aliases))
				&& id == inventoryId;
	}

	private boolean contains(List<String> a, List<String> b) {
		for (String c : a)
			for (String d : b)
				if (d.equalsIgnoreCase(c))
					return true;
		return false;
	}

}
