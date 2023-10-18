package fr.maxlego08.zshop.api.buttons;

import fr.maxlego08.zshop.api.economy.ShopEconomy;

import java.util.List;

public interface ItemConfirmButton extends EconomyAction {

    double getPrice();

    ShopEconomy getShopEconomy();

    List<String> getCommands();

    boolean enableLog();

    List<String> getConfirmMessages();

    String getName();

}
