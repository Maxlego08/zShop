package fr.maxlego08.zshop.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.buttons.RemoveButton;
import fr.maxlego08.zshop.buttons.ZRemoveButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class RemoveButtonLoader implements ButtonLoader {

    private final ShopPlugin plugin;

    public RemoveButtonLoader(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return RemoveButton.class;
    }

    @Override
    public String getName() {
        return "zshop_remove";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path) {

        String amount = configuration.getString(path + "amount", "1");

        return new ZRemoveButton(this.plugin, amount);
    }
}
