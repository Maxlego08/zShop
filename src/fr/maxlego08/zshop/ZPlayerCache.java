package fr.maxlego08.zshop;

import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.buttons.ItemButton;

public class ZPlayerCache implements PlayerCache {

    private ItemButton itemButton;
    private int amount = 1;

    @Override
    public ItemButton getItemButton() {
        return itemButton;
    }

    @Override
    public void setItemButton(ItemButton itemButton) {
        this.itemButton = itemButton;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setItemAmount(int amount) {
        this.amount = amount;
    }
}
