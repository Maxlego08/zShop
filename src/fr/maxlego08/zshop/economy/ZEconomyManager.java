package fr.maxlego08.zshop.economy;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.economy.EconomyManager;
import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.economy.economies.BeastTokenEconomy;
import fr.maxlego08.zshop.economy.economies.CoinsEngineEconomy;
import fr.maxlego08.zshop.economy.economies.ExperienceEconomy;
import fr.maxlego08.zshop.economy.economies.ItemEconomy;
import fr.maxlego08.zshop.economy.economies.LevelEconomy;
import fr.maxlego08.zshop.economy.economies.PlayerPointEconomy;
import fr.maxlego08.zshop.economy.economies.TokenManagerEconomy;
import fr.maxlego08.zshop.economy.economies.VaultEconomy;
import fr.maxlego08.zshop.economy.economies.VotingEconomy;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.zcore.logger.Logger;
import fr.maxlego08.zshop.zcore.utils.plugins.Plugins;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ZEconomyManager implements EconomyManager {

    private final ShopPlugin plugin;
    private final List<ShopEconomy> shopEconomies = new ArrayList<>();

    public ZEconomyManager(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Collection<ShopEconomy> getEconomies() {
        return Collections.unmodifiableCollection(this.shopEconomies);
    }

    @Override
    public boolean registerEconomy(ShopEconomy economy) {
        Optional<ShopEconomy> optional = getEconomy(economy.getName());
        System.out.println("Try register " + economy.getName() + " - " + optional);
        if (!optional.isPresent()) {
            System.out.println("Register " + economy.getName());
            this.shopEconomies.add(economy);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEconomy(ShopEconomy economy) {
        return this.shopEconomies.remove(economy);
    }

    @Override
    public Optional<ShopEconomy> getEconomy(String economyName) {
        return this.shopEconomies.stream().filter(e -> e.getName().equalsIgnoreCase(economyName)).findFirst();
    }

    @Override
    public void loadEconomies() {

        File file = new File(this.plugin.getDataFolder(), "economies.yml");
        if (!file.exists()) {
            this.plugin.saveResource("economies.yml", true);
        }

        this.shopEconomies.clear();

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        for (String key : configuration.getConfigurationSection("economies.").getKeys(false)) {
            String path = "economies." + key + ".";

            if (!configuration.getBoolean(path + "isEnable")) continue;

            String name = configuration.getString(path + "name", "VAULT");
            String type = configuration.getString(path + "type", "VAULT");
            String currency = configuration.getString(path + "currency", "$");
            String denyMessage = configuration.getString(path + "denyMessage");

            switch (type.toLowerCase()) {
                case "vault":
                    if (Config.enableDebug) Logger.info("Register Vault economy");
                    registerEconomy(new VaultEconomy(this.plugin, name, currency, denyMessage));
                    break;
                case "item":
                    if (Config.enableDebug) Logger.info("Register Item economy with name " + name);
                    Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getIManager());
                    try {
                        MenuItemStack menuItemStack = loader.load(configuration, path + "item.", file);
                        registerEconomy(new ItemEconomy(name, currency, denyMessage, menuItemStack));
                    } catch (InventoryException exception) {
                        Logger.info("Error with " + path + ".item. economy !", Logger.LogType.ERROR);
                    }
                    break;
                case "level":
                    if (Config.enableDebug) Logger.info("Register Level economy with name " + name);
                    registerEconomy(new LevelEconomy(name, currency, denyMessage));
                    break;
                case "beasttoken":
                    if (Config.enableDebug) Logger.info("Register BeastToken economy with name " + name);
                    registerEconomy(new BeastTokenEconomy(name, currency, denyMessage));
                    break;
                case "experience":
                    if (Config.enableDebug) Logger.info("Register Experience economy with name " + name);
                    registerEconomy(new ExperienceEconomy(name, currency, denyMessage));
                    break;
                case "playerpoints":
                    if (this.plugin.isEnable(Plugins.PLAYERPOINT)) {
                        if (Config.enableDebug) Logger.info("Register PlayerPoints economy");
                        registerEconomy(new PlayerPointEconomy(this.plugin, name, currency, denyMessage));
                    } else Logger.info("Try to register PlayerPoints economy but PlayerPoints plugin is not enable");
                    break;
                case "votingplugin":
                    if (this.plugin.isEnable(Plugins.VOTINGPLUGIN)) {
                        if (Config.enableDebug) Logger.info("Register VotingPlugin economy");
                        registerEconomy(new VotingEconomy(name, currency, denyMessage));
                    } else Logger.info("Try to register VotingPlugin economy but VotingPlugin plugin is not enable");
                    break;
                case "tokenmanager":
                    if (this.plugin.isEnable(Plugins.TOKENMANAGER)) {
                        if (Config.enableDebug) Logger.info("Register TokenManager economy");
                        registerEconomy(new TokenManagerEconomy(this.plugin, name, currency, denyMessage));
                    } else Logger.info("Try to register TokenManager economy but TokenManager plugin is not enable");
                    break;
                case "coinsengine":
                    if (this.plugin.isEnable(Plugins.COINSENGINE)) {
                        if (Config.enableDebug) Logger.info("Register CoinsEngine economy");

                        String currencyName = configuration.getString(path + "currencyName");
                        registerEconomy(new CoinsEngineEconomy(this.plugin, name, currency, denyMessage, currencyName));

                    } else Logger.info("Try to register CoinsEngine economy but CoinsEngine plugin is not enable");
                    break;
                default:
                    break;
            }
        }
    }
}
