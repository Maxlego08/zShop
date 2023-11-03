package fr.maxlego08.zshop.api.economy;

import java.util.Collection;
import java.util.Optional;

public interface EconomyManager {

    Collection<ShopEconomy> getEconomies();

    boolean registerEconomy(ShopEconomy economy);

    boolean removeEconomy(ShopEconomy economy);

    Optional<ShopEconomy> getEconomy(String economyName);

    void loadEconomies();
}
