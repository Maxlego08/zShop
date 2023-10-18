package fr.maxlego08.zshop;

import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.PriceType;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.api.buttons.EconomyAction;
import fr.maxlego08.zshop.api.utils.PriceModifierCache;

import java.util.Optional;

public class ZPlayerCache implements PlayerCache {

    private ItemButton itemButton;
    private EconomyAction economyAction;
    private int amount = 1;
    private PriceModifierCache sellCache = new PriceModifierCache(0, Optional.empty());
    private PriceModifierCache buyCache = new PriceModifierCache(0, Optional.empty());

    @Override
    public ItemButton getItemButton() {
        return itemButton;
    }

    @Override
    public void setItemButton(ItemButton itemButton) {
        this.itemButton = itemButton;
    }

    @Override
    public EconomyAction getEconomyAction() {
        return this.economyAction;
    }

    @Override
    public void setEconomyAction(EconomyAction economyAction) {
        this.economyAction = economyAction;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setItemAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public PriceModifierCache getPriceModifier(PriceType priceType) {
        return priceType == PriceType.SELL ? sellCache : buyCache;
    }

    @Override
    public void setPriceModifier(PriceType priceType, PriceModifierCache cache) {
        if (priceType == PriceType.SELL) sellCache = cache;
        else buyCache = cache;
    }
}
