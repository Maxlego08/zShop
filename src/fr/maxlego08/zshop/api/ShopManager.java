package fr.maxlego08.zshop.api;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

    Optional<PriceModifier> getPriceModifier(Player player, PriceType priceType);

    Optional<ItemButton> getItemButton(Material material);

    Optional<ItemButton> getItemButton(String material);

    Collection<ItemButton> getItemButtons();

    Consumer<Button> getButtonListener();
}