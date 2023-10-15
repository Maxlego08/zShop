package fr.maxlego08.zshop.api;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ShopManager extends Listener {

    /**
     * Register Placeholders
     */
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

    /**
     * Load configs
     */
    void loadConfig();

    /**
     * Return the default plugin lore
     *
     * @return lore
     */
    List<String> getDefaultLore();

    /**
     * Transform a price to a string
     *
     * @param price current price
     * @return format price
     */
    String transformPrice(double price);

    /**
     * Open buy inventory
     *
     * @param player     the player
     * @param itemButton the current item button
     */
    void openBuy(Player player, ItemButton itemButton);

    /**
     * Open sell inventory
     *
     * @param player     the player
     * @param itemButton the current item button
     */
    void openSell(Player player, ItemButton itemButton);

    /**
     * Allows to return the cache of the player, data will be saved in cache according to the player to avoid having to calculate several times' data.
     *
     * @param player the player
     * @return PlayerCache
     */
    PlayerCache getCache(Player player);

    /**
     * Allows you to return the {@link PriceModifier} a player for a {@link PriceType}
     *
     * @param player    the Player
     * @param priceType SELL or BUY
     * @return optional of {@link PriceModifier}
     */
    Optional<PriceModifier> getPriceModifier(Player player, PriceType priceType);

    /**
     * Get an ItemButton
     *
     * @param material {@link Material}
     * @return optional of {@link ItemButton}
     */
    Optional<ItemButton> getItemButton(Material material);

    /**
     * Get an ItemButton
     *
     * @param material as string
     * @return optional of {@link ItemButton}
     */
    Optional<ItemButton> getItemButton(String material);

    /**
     * Get an ItemButton
     *
     * @param player the player
     * @param itemStack Current item
     * @return optional of {@link ItemButton}
     */
    Optional<ItemButton> getItemButton(Player player, ItemStack itemStack);

    /**
     * Retrieves the list of items button used in inventories
     *
     * @return collection of {@link ItemButton}
     */
    Collection<ItemButton> getItemButtons();

    /**
     * Returns the listener for button register
     * Allows you to cache ItemButtons
     *
     * @return consumer
     */
    Consumer<Button> getButtonListener();

    /**
     * Sell all the contents of its inventory
     *
     * @param player Player
     */
    void sellAll(Player player);

    /**
     * Set item in hand
     *
     * @param player the player
     * @param amount current amount
     */
    void sellHand(Player player, int amount);

    /**
     * Sells all items inventory which are the same as the one being held in
     * your hand
     *
     * @param player the Player
     */
    void sellAllHand(Player player);
}