package fr.maxlego08.shop.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.command.commands.CommandShop;
import fr.maxlego08.shop.command.commands.CommandShopReload;
import fr.maxlego08.shop.command.commands.CommandShopTest;
import fr.maxlego08.shop.command.commands.CommandShopVersion;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.inventory.IIventory;

public class CommandManager implements CommandExecutor {

	private final ZShop main;
	private final List<VCommand> commands = new ArrayList<VCommand>();

	public CommandManager(ZShop template) {
		this.main = template;
		this.registerCommands();
		this.commandChecking();
	}

	private void registerCommands() {

		VCommand command = addCommand("shop", new CommandShop()
				.setConsoleCanUse(false)
				.addSubCommand("zshop")
				.setPermission("zshop.use")
				.setDescription("Open shop")
				.setSyntaxe("/shop"));
		
		addCommand(new CommandShopVersion()
				.setParent(command)
				.addSubCommand("version", "v", "ver", "?")
				.setSyntaxe("/shop version")
				.setDescription("Show plugin version"));
		
		addCommand(new ZCommand()
				.sendHelp("shop")
				.addSubCommand("help")
				.setSyntaxe("/shop help")
				.setDescription("Show help")
				.setPermission("zshop.help")
				.setParent(command));
		
		addCommand(new CommandShopReload()
				.addSubCommand("reload")
				.setSyntaxe("/shop reload")
				.setDescription("Reload plugin")
				.setPermission("zshop.reload")
				.setParent(command));
		
		addCommand(new CommandShopTest().setParent(command).addSubCommand("test").setPermission("zshop.test"));
		
		main.getLog().log("Loading " + getUniqueCommand() + " commands", LogType.SUCCESS);
	}

	public VCommand addCommand(VCommand command) {
		commands.add(command);
		return command;
	}

	public VCommand addCommand(String string, VCommand command) {
		commands.add(command.addSubCommand(string));
		ZPlugin.z().getCommand(string).setExecutor(this);
		return command;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		for (VCommand command : commands) {
			if (command.getSubCommands().contains(cmd.getName().toLowerCase())) {
				if ((args.length == 0 || command.isIgnoreParent()) && command.getParent() == null) {
					CommandType type = processRequirements(command, sender, args);
					if (!type.equals(CommandType.CONTINUE))
						return true;
				}
			} else if (args.length >= 1 && command.getParent() != null
					&& command.getParent().getSubCommands().contains(cmd.getName().toLowerCase())
					&& canExecute(args, cmd.getName().toLowerCase(), command)) {
				CommandType type = processRequirements(command, sender, args);
				if (!type.equals(CommandType.CONTINUE))
					return true;
			}
		}
		sender.sendMessage(Lang.prefix + " " + Lang.commandError);
		return true;
	}

	/**
	 * @param args
	 * @param cmd
	 * @param command
	 * @return true if can execute
	 */
	private boolean canExecute(String[] args, String cmd, VCommand command) {
		for (int index = args.length - 1; index > -1; index--) {
			if (command.getSubCommands().contains(args[index].toLowerCase())) {
				if (command.isIgnoreArgs())
					return true;
				if (index < args.length - 1)
					return false;
				return canExecute(args, cmd, command.getParent(), index - 1);
			}
		}
		return false;
	}

	/**
	 * @param args
	 * @param cmd
	 * @param command
	 * @param index
	 * @return
	 */
	private boolean canExecute(String[] args, String cmd, VCommand command, int index) {
		if (index < 0 && command.getSubCommands().contains(cmd.toLowerCase()))
			return true;
		else if (index < 0)
			return false;
		else if (command.getSubCommands().contains(args[index].toLowerCase()))
			return canExecute(args, cmd, command.getParent(), index - 1);
		else
			return false;
	}

	/**
	 * @param command
	 * @param sender
	 * @param strings
	 * @return
	 */
	private CommandType processRequirements(VCommand command, CommandSender sender, String[] strings) {

		if (!(sender instanceof Player) && !command.isConsoleCanUse()) {
			sender.sendMessage(Lang.prefix + " " + Lang.onlinePlayerCanUse);
			return CommandType.DEFAULT;
		}
		if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {
			if (command.getArgsMinLength() != 0 && command.getArgsMaxLength() != 0
					&& !(strings.length >= command.getArgsMinLength()
							&& strings.length <= command.getArgsMaxLength())) {
				if (command.getSyntaxe() != null)
					sender.sendMessage(
							Lang.prefix + " " + Lang.syntaxeError.replace("%command%", command.getSyntaxe()));
				return CommandType.SYNTAX_ERROR;
			}
			command.setSender(sender);
			command.setArgs(strings);
			if (command.getInventory() != null && sender instanceof Player) {
				IIventory iIventory = command.getInventory();
				main.getInventoryManager().createInventory(iIventory.getId(), command.getPlayer(), iIventory.getPage(),
						iIventory.getArgs());
			}
			CommandType returnType = command.perform(main);
			if (returnType == CommandType.SYNTAX_ERROR) {
				sender.sendMessage(Lang.prefix + " " + Lang.syntaxeError.replace("%command%", command.getSyntaxe()));
			}
			return returnType;
		}
		sender.sendMessage(Lang.prefix + " " + Lang.noPermission);
		return CommandType.DEFAULT;
	}

	public List<VCommand> getCommands() {
		return commands;
	}

	private int getUniqueCommand() {
		return (int) commands.stream().filter(command -> command.getParent() == null).count();
	}

	/**
	 * @param commandString
	 * @param sender
	 */
	public void sendHelp(String commandString, CommandSender sender) {
		commands.forEach(command -> {
			if ((command.getSubCommands().contains(commandString)
					|| command.getParent() != null && command.getParent().getSubCommands().contains(commandString))
					&& command.getDescription() != null) {
				sender.sendMessage(Lang.commandHelp.replace("%syntaxe%", command.getSyntaxe()).replace("%description%",
						command.getDescription()));
			}
		});
	}

	/**
	 * Check if your order is ready for use
	 */
	private void commandChecking() {
		commands.forEach(command -> {
			if (command.sameSubCommands()) {
				Logger.info(command.toString() + " command to an argument similar to its parent command !",
						LogType.ERROR);
				ZPlugin.z().getPluginLoader().disablePlugin(ZPlugin.z());
			}
		});
	}

}