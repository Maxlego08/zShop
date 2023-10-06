package fr.maxlego08.zshop.api;

import fr.maxlego08.zshop.api.buttons.ItemButton;

public interface PlayerCache {

    ItemButton getItemButton();

    void setItemButton(ItemButton itemButton);

    int getAmount();

    void setItemAmount(int amount);

}
