package fr.maxlego08.zshop;

import fr.maxlego08.zshop.command.commands.CommandShop;
import fr.maxlego08.zshop.placeholder.LocalPlaceholder;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.save.MessageLoader;
import fr.maxlego08.zshop.zcore.ZPlugin;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class ShopPlugin extends ZPlugin {

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zshop");

        this.preEnable();

        this.registerCommand("zshop", new CommandShop(this), "zpl");

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

}
