package fr.maxlego08.zshop.command.commands;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.command.VCommand;
import fr.maxlego08.zshop.zcore.enums.Permission;
import fr.maxlego08.zshop.zcore.utils.commands.CommandType;

public class CommandSellAllHand extends VCommand {

    public CommandSellAllHand(ShopPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSHOP_SELL_HAND_ALL);
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(ShopPlugin plugin) {
        this.plugin.getShopManager().sellAllHand(this.player);
        return CommandType.SUCCESS;
    }

}
