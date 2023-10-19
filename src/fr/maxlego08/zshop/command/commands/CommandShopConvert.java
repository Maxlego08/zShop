package fr.maxlego08.zshop.command.commands;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.command.VCommand;
import fr.maxlego08.zshop.convert.ShopGuiPlusConverter;
import fr.maxlego08.zshop.zcore.enums.Message;
import fr.maxlego08.zshop.zcore.enums.Permission;
import fr.maxlego08.zshop.zcore.utils.commands.CommandType;

public class CommandShopConvert extends VCommand {

    private final ShopGuiPlusConverter shopGuiPlusConverter = new ShopGuiPlusConverter();

    public CommandShopConvert(ShopPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSHOP_CONVERT);
        this.addSubCommand("convert");
        this.setDescription(Message.DESCRIPTION_CONVERT);
    }

    @Override
    protected CommandType perform(ShopPlugin plugin) {
        this.shopGuiPlusConverter.convert(plugin, sender);
        return CommandType.SUCCESS;
    }

}
