package fr.maxlego08.zshop.economy;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.economy.EconomyManager;
import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.api.event.events.ZShopEconomyRegisterEvent;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.zcore.logger.Logger;
import fr.traqueur.currencies.Currencies;
import fr.traqueur.currencies.CurrencyProvider;
import fr.traqueur.currencies.providers.ZEssentialsProvider;
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
        if (!optional.isPresent()) {
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

            Currencies currencies = Currencies.valueOf(type.toUpperCase());
            CurrencyProvider currencyProvider = null;

            switch (currencies) {
                case ZMENUITEMS:
                case ITEM:
                    currencyProvider = Currencies.ZMENUITEMS.createProvider(plugin, file, path + "item.");
                    break;
                case ZESSENTIALS:
                case ECOBITS:
                case COINSENGINE:
                case REDISECONOMY:
                    String currencyName = configuration.getString(path + "currencyName");
                    currencyProvider = currencies.createProvider(currencyName);
                    break;
                default:
                    currencyProvider = currencies.createProvider();
                    break;
            }

            if (Config.enableDebug) Logger.info("Register Vault economy");
            registerEconomy(new ZShopEconomy(name, currency, denyMessage, currencyProvider));
        }

        ZShopEconomyRegisterEvent event = new ZShopEconomyRegisterEvent(this);
        event.call();
    }
}
