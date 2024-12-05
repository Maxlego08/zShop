/**
 * Interface used to interact with the economy
 * <p>
 * This interface is used to interact with the economy, like getting the money of a player, depositing money to a player, etc
 */
package fr.maxlego08.zshop.api.economy;

import org.bukkit.OfflinePlayer;

public interface ShopEconomy {

    /**
     * Get the money of a player
     * <p>
     * This method is used to get the money of a player
     *
     * @param offlinePlayer the player
     * @return the money of the player
     */
    double getMoney(OfflinePlayer offlinePlayer);

    /**
     * Check if a player has enough money
     * <p>
     * This method is used to check if a player has enough money
     *
     * @param offlinePlayer the player
     * @param price         the price to check
     * @return true if the player has enough money, false otherwise
     */
    boolean hasMoney(OfflinePlayer offlinePlayer, double price);

    /**
     * Deposit money to a player
     * <p>
     * This method is used to give money to a player
     *
     * @param offlinePlayer the player
     * @param value         the amount of money to give
     * @param reason        the reason of the deposit
     */
    void depositMoney(OfflinePlayer offlinePlayer, double value, String reason);

    /**
     * Withdraw money from a player
     * <p>
     * This method is used to take money from a player
     *
     * @param offlinePlayer the player
     * @param value         the amount of money to take
     * @param reason        the reason of the withdraw
     */
    void withdrawMoney(OfflinePlayer offlinePlayer, double value, String reason);

    /**
     * Get the currency of the economy
     * <p>
     * This method is used to get the currency of the economy
     * <p>
     * The currency is used for display in messages and inventories
     *
     * @return the currency of the economy
     */
    String getCurrency();

    /**
     * Get the name of the economy
     * <p>
     * This method is used to get the name of the economy
     *
     * @return the name of the economy
     */
    String getName();

    /**
     * Get the message sent when the player does not have enough money
     * <p>
     * This method is used to get the message sent when the player does not have enough money
     *
     * @return the message
     */
    String getDenyMessage();

    default String format(String priceAsString, double amount) {
        return getCurrency().replace("%price%", priceAsString).replace("%s%", amount > 1 ? "s" : "");
    }

}