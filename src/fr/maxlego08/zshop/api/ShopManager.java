package fr.maxlego08.zshop.api;

import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public interface ShopManager extends Listener {

    void registerPlaceholders();

    /**
     * Load inventories
     */
    void loadInventories();

    /**
     * Load patterns
     */
    void loadPatterns();

    /**
     * Load commands
     */
    void loadCommands();

    void loadConfig();

    List<String> getDefaultLore();

    String transformPrice(double price);

    void openBuy(Player player, ItemButton itemButton);

    void openSell(Player player, ItemButton itemButton);

    PlayerCache getCache(Player player);
}