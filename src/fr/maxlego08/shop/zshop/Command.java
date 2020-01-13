package fr.maxlego08.shop.zshop;

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

}
