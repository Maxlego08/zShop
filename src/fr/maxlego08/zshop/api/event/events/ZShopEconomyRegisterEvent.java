package fr.maxlego08.zshop.api.event.events;

import fr.maxlego08.zshop.api.economy.EconomyManager;
import fr.maxlego08.zshop.api.event.ShopEvent;
import fr.maxlego08.zshop.economy.ZEconomyManager;

public class ZShopEconomyRegisterEvent extends ShopEvent {

    private final EconomyManager manager;

    public ZShopEconomyRegisterEvent(EconomyManager manager) {
        this.manager = manager;
    }

    public EconomyManager getManager() {
        return manager;
    }
}
