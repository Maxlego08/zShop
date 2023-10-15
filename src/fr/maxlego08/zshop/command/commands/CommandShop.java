package fr.maxlego08.zshop.command.commands;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.command.VCommand;
import fr.maxlego08.zshop.zcore.enums.Permission;
import fr.maxlego08.zshop.zcore.utils.commands.CommandType;

public class CommandShop extends VCommand {

    public CommandShop(ShopPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSHOP_HELP);
        this.addSubCommand(new CommandShopReload(plugin));
        this.addSubCommand(new CommandShopLogs(plugin));
    }

    @Override
    protected CommandType perform(ShopPlugin plugin) {

        syntaxMessage();
        return CommandType.SUCCESS;
    }

}
