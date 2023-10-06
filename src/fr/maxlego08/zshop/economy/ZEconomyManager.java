package fr.maxlego08.zshop.economy;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.economy.EconomyManager;
import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.economy.economies.VaultEconomy;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.zcore.logger.Logger;
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
            shopEconomies.add(economy);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEconomy(ShopEconomy economy) {
        return shopEconomies.remove(economy);
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

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        for (String key : configuration.getConfigurationSection("economies.").getKeys(false)) {
            String path = "economies." + key + ".";

            if (!configuration.getBoolean(path + "isEnable")) continue;

            String name = configuration.getString(path + "name", "VAULT");
            String format = configuration.getString(path + "format", "v");
            String currency = configuration.getString(path + "currency", "$");
            String denyMessage = configuration.getString(path + "denyMessage");

            switch (name.toLowerCase()) {
                case "vault":
                    if (Config.enableDebug) Logger.info("Register Vault economy");
                    registerEconomy(new VaultEconomy(this.plugin, name, currency, format, denyMessage));
                    break;
                case "---":
                    // TODO
                    break;
                default:
                    break;
            }

        }
    }
}
