package fr.maxlego08.zshop.api.economy;

import org.bukkit.OfflinePlayer;

public interface ShopEconomy {

    /**
     * Permet de récupérer l'argent du joueur
     *
     * @param offlinePlayer
     * @return double
     */
    double getMoney(OfflinePlayer offlinePlayer);

    /**
     * Allows you to check if a player has enough money
     *
     * @param offlinePlayer
     * @param price
     * @return boolean
     */
    boolean hasMoney(OfflinePlayer offlinePlayer, double price);

    /**
     * Allows you to give money to a player
     *
     * @param offlinePlayer
     * @param value
     */
    void depositMoney(OfflinePlayer offlinePlayer, double value);

    /**
     * Allows you to withdraw money from a player
     *
     * @param offlinePlayer
     * @param value
     */
    void withdrawMoney(OfflinePlayer offlinePlayer, double value);

    /**
     * Allows you to obtain the currency of the economy
     * Is used for display in messages and inventories
     *
     * @return string
     */
    String getCurrency();

    /**
     * Returns the format of the savings to be used in the sales command
     *
     * @return string
     */
    String getFormat();

    /**
     * Get economy name
     *
     * @return string
     */
    String getName();

}
