package fr.maxlego08.zshop.api.event.events;

import fr.maxlego08.zshop.api.event.ShopEvent;
import fr.maxlego08.zshop.economy.ZEconomyManager;

public class ZShopEconomyRegisterEvent extends ShopEvent {

    private final ZEconomyManager manager;

    public ZShopEconomyRegisterEvent(ZEconomyManager manager) {
        this.manager = manager;
    }

    public ZEconomyManager getManager() {
        return manager;
    }
}
