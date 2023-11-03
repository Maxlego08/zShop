package fr.maxlego08.zshop.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.buttons.AddButton;
import fr.maxlego08.zshop.buttons.ZBuyMore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class BuyMoreLoader implements ButtonLoader {

    private final ShopPlugin plugin;

    public BuyMoreLoader(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return AddButton.class;
    }

    @Override
    public String getName() {
        return "zshop_buy_more";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        int amount = configuration.getInt(path + "amount", 1);

        return new ZBuyMore(plugin, amount);
    }
}
