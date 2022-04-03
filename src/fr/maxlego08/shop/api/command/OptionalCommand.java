package fr.maxlego08.shop.api.command;

import org.bukkit.command.CommandSender;

import fr.maxlego08.shop.api.enums.OptionalAction;

public class OptionalCommand {

	private final OptionalAction action;
	private final String permission;
	private final String description;

	public OptionalCommand(OptionalAction action, String permission, String description) {
		super();
		this.action = action;
		this.permission = permission;
		this.description = description;
	}

	public OptionalAction getAction() {
		return action;
	}

	public String getPermission() {
		return permission;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPresent() {
		return action != null;
	}

	public static OptionalCommand empty(){
		return new OptionalCommand(null, null, null);
	}

	public boolean hasPermission(CommandSender sender) {
		return permission != null && sender.hasPermission(permission);
	}
	
}
