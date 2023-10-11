package fr.maxlego08.zshop.api;

import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.api.utils.PriceModifierCache;

public interface PlayerCache {

    ItemButton getItemButton();

    void setItemButton(ItemButton itemButton);

    int getAmount();

    void setItemAmount(int amount);

    PriceModifierCache getPriceModifier(PriceType priceType);

    void setPriceModifier(PriceType priceType, PriceModifierCache cache);

}
