package fr.maxlego08.zshop.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.buttons.ShowItemButton;
import fr.maxlego08.zshop.buttons.ZShowItemButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ShowItemButtonLoader implements ButtonLoader {

    private final ShopPlugin plugin;

    public ShowItemButtonLoader(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return ShowItemButton.class;
    }

    @Override
    public String getName() {
        return "zshop_show";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path) {

        List<String> lore = configuration.getStringList(path + "lore");

        return new ZShowItemButton(this.plugin, lore);
    }
}
