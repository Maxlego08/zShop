package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.zcore.utils.ZUtils;
import org.bukkit.OfflinePlayer;

public abstract class DefaultExample extends ZUtils implements ShopEconomy {

    private final String name;
    private final String currency;
    private final String denyMessage;

    public DefaultExample(String name, String currency, String denyMessage) {
        this.name = name;
        this.currency = currency;
        this.denyMessage = denyMessage;
    }

    @Override
    public String getCurrency() {
        return this.currency;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDenyMessage() {
        return this.denyMessage;
    }

    @Override
    public boolean hasMoney(OfflinePlayer offlinePlayer, double price) {
        return getMoney(offlinePlayer) >= price;
    }
}
