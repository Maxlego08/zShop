package fr.maxlego08.zshop.api.buttons;

import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.api.history.HistoryType;
import fr.maxlego08.zshop.api.limit.Limit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemButton extends EconomyAction {

    double getSellPrice();

    double getBuyPrice();

    double getSellPrice(int amount);

    double getBuyPrice(int amount);

    double getSellPrice(Player player, int amount);

    double getBuyPrice(Player player, int amount);

    double getUnitSellPrice();

    double getUnitBuyPrice();

    boolean canSell();

    boolean canBuy();

    int getMaxStack();

    ShopEconomy getEconomy();

    List<String> getLore();

    /**
     * Allows to recover the sale price with the modifiers
     *
     * @param player the player
     * @param amount current amount
     * @return price format
     */
    String getSellPriceFormat(Player player, int amount);

    /**
     * Allows to recover the purchase price with the modifiers
     *
     * @param player the player
     * @param amount current amount
     * @return price format
     */
    String getBuyPriceFormat(Player player, int amount);

    /**
     * Allows to recover the sale price
     *
     * @param amount current amount
     * @return price format
     */
    String getSellPriceFormat(int amount);

    /**
     * Allows to recover the purchase price
     *
     * @param amount current amount
     * @return price format
     */
    String getBuyPriceFormat(int amount);

    /**
     * @return buy commands
     */
    List<String> getBuyCommands();

    /**
     * @return sell commands
     */
    List<String> getSellCommands();

    /**
     * @return give item
     */
    boolean giveItem();

    Optional<Limit> getServerBuyLimit();

    Optional<Limit> getServerSellLimit();

    Optional<Limit> getPlayerBuyLimit();

    Optional<Limit> getPlayerSellLimit();

    boolean enableLog();

    void log(int amount, String itemName, String price, String playerName, UUID uuid, HistoryType type);

    boolean affectByPriceModifier();

    String getMob();

    String nbtMobSpawnerKey = "zshop-mob";

}
