package fr.maxlego08.zshop.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.api.limit.Limit;
import fr.maxlego08.zshop.api.limit.LimitType;
import fr.maxlego08.zshop.buttons.ZItemButton;
import fr.maxlego08.zshop.exceptions.EconomyNotFoundException;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.zcore.utils.loader.Loader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class ItemButtonLoader implements ButtonLoader {

    private final ShopPlugin plugin;

    public ItemButtonLoader(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return ItemButton.class;
    }

    @Override
    public String getName() {
        return "zshop_item";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path) {

        double sellPrice = configuration.getDouble(path + "sellPrice", 0.0);
        double buyPrice = configuration.getDouble(path + "buyPrice", 0.0);

        int maxStack = configuration.getInt(path + "maxStack", 64);
        List<String> lore = configuration.getStringList(path + "lore");
        boolean giveItem = configuration.getBoolean(path + "giveItem", true);
        boolean enableLog = configuration.getBoolean(path + "enableLog", true);
        boolean affectByPriceModifier = configuration.getBoolean(path + "affectByPriceModifier", true);
        List<String> buyCommands = configuration.getStringList(path + "buyCommands");
        List<String> sellCommands = configuration.getStringList(path + "sellCommands");

        String economyName = configuration.getString(path + "economy", Config.defaultEconomy);
        Optional<ShopEconomy> optional = plugin.getEconomyManager().getEconomy(economyName);
        if (!optional.isPresent()) {
            throw new EconomyNotFoundException("Economy " + economyName + " was not found for button " + path);
        }
        ShopEconomy shopEconomy = optional.get();

        if (lore.isEmpty()) lore = this.plugin.getShopManager().getDefaultLore();

        Limit serverSellLimit = null;
        Limit serverBuyLimit = null;
        Limit playerSellLimit = null;
        Limit playerBuyLimit = null;
        String material = configuration.getString(path + "item.material", "STONE");

        if (configuration.contains(path + "serverSellLimit")) {
            Loader<Limit> loader = new LimitLoader(plugin, material, LimitType.SERVER_SELL);
            serverSellLimit = loader.load(configuration, path + "serverSellLimit.");
            this.plugin.getLimiterManager().create(serverSellLimit);
        }

        if (configuration.contains(path + "serverBuyLimit")) {
            Loader<Limit> loader = new LimitLoader(plugin, material, LimitType.SERVER_BUY);
            serverBuyLimit = loader.load(configuration, path + "serverBuyLimit.");
            this.plugin.getLimiterManager().create(serverBuyLimit);
        }

        if (configuration.contains(path + "playerSellLimit")) {
            Loader<Limit> loader = new LimitLoader(plugin, material, LimitType.PLAYER_SELL);
            playerSellLimit = loader.load(configuration, path + "playerSellLimit.");
            this.plugin.getLimiterManager().create(playerSellLimit);
        }

        if (configuration.contains(path + "playerBuyLimit")) {
            Loader<Limit> loader = new LimitLoader(plugin, material, LimitType.PLAYER_BUY);
            playerBuyLimit = loader.load(configuration, path + "playerBuyLimit.");
            this.plugin.getLimiterManager().create(playerBuyLimit);
        }

        return new ZItemButton(plugin, sellPrice, buyPrice, maxStack, lore, shopEconomy, buyCommands, sellCommands, giveItem, serverSellLimit, serverBuyLimit, playerSellLimit, playerBuyLimit, enableLog, affectByPriceModifier);
    }
}
