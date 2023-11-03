package fr.maxlego08.zshop.api.event.events;

import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.api.event.CancelledShopEvent;
import org.bukkit.entity.Player;

public class ZShopBuyEvent extends CancelledShopEvent {

    private final Player player;
    private final ItemButton itemButton;
    private int amount;
    private double price;
    private int serverLimit;
    private int playerLimit;

    public ZShopBuyEvent(Player player, ItemButton itemButton, int amount, double price, int serverLimit, int playerLimit) {
        this.player = player;
        this.itemButton = itemButton;
        this.amount = amount;
        this.price = price;
        this.serverLimit = serverLimit;
        this.playerLimit = playerLimit;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemButton getItemButton() {
        return itemButton;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getServerLimit() {
        return serverLimit;
    }

    public void setServerLimit(int serverLimit) {
        this.serverLimit = serverLimit;
    }

    public int getPlayerLimit() {
        return playerLimit;
    }

    public void setPlayerLimit(int playerLimit) {
        this.playerLimit = playerLimit;
    }
}
