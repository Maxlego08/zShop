package fr.maxlego08.zshop.api.event;

import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.inventory.ItemStack;

public class ShopAction {

    private final ItemStack itemStack;
    private final ItemButton itemButton;
    private double price;

    public ShopAction(ItemStack itemStack, ItemButton itemButton, double price) {
        this.itemStack = itemStack;
        this.itemButton = itemButton;
        this.price = price;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemButton getItemButton() {
        return itemButton;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
