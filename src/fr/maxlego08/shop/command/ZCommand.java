package fr.maxlego08.shop.command;

import java.util.List;
import java.util.function.BiConsumer;

import fr.maxlego08.shop.ZShop;

public class ZCommand extends VCommand {

	private BiConsumer<VCommand, ZShop> command;

	@Override
	public CommandType perform(ZShop main) {
		
		if (command != null){
			command.accept(this, main);
		}

		return CommandType.SUCCESS;
	}

	public VCommand setCommand(BiConsumer<VCommand, ZShop> command) {
		this.command = command;
		return this;
	}

	public VCommand msg(String message) {
		command = (cmd, main) -> cmd.sendMessage(message);
		return this;
	}

	public VCommand msg(List<String> message) {
		command = (cmd, main) -> cmd.sendMessage(message);
		return this;
	}

	public VCommand sendHelp(String command) {
		this.command = (cmd, main) -> main.getCommandManager().sendHelp(command, cmd.getSender());
		return this;
	}

}
