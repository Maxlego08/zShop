package fr.maxlego08.zshop.economy.economies;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.zshop.zcore.logger.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class ItemEconomy extends DefaultExample {

    private final MenuItemStack menuItemStack;

    public ItemEconomy(String name, String currency, String format, String denyMessage, MenuItemStack itemStack) {
        super(name, currency, format, denyMessage);
        this.menuItemStack = itemStack;
    }

    @Override
    public double getMoney(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            return getAmount(player, menuItemStack.build(player));
        } else return 0.0;
    }

    @Override
    public void depositMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            giveItem(player, (int) value, menuItemStack.build(player));
        } else Logger.info("Deposit items to " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
    }

    @Override
    public void withdrawMoney(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            removeItems(player, menuItemStack.build(player), (int) value);
        } else Logger.info("Withdraw items from " + offlinePlayer.getName() + " but is offline", Logger.LogType.ERROR);
    }

    private int getAmount(Player player, ItemStack itemStack) {
        int items = 0;
        for (int slot = 0; slot != 36; slot++) {
            ItemStack currentItemStack = player.getInventory().getItem(slot);
            if (currentItemStack != null && currentItemStack.isSimilar(itemStack))
                items += currentItemStack.getAmount();
        }
        return items;
    }

    private void removeItems(Player player, ItemStack itemStack, long value) {
        PlayerInventory playerInventory = player.getInventory();

        int item = (int) value;
        int slot = 0;

        // On retire ensuite les items de l'inventaire du joueur
        for (ItemStack is : playerInventory.getContents()) {

            if (is != null && is.isSimilar(itemStack) && item > 0) {

                int currentAmount = is.getAmount() - item;
                item -= is.getAmount();

                if (currentAmount <= 0) {
                    if (slot == 40)
                        playerInventory.setItemInOffHand(null);
                    else
                        playerInventory.removeItem(is);
                } else
                    is.setAmount(currentAmount);
            }
            slot++;
        }
    }

    private void giveItem(Player player, long value, ItemStack itemStack) {
        itemStack = itemStack.clone();
        if (value > 64) {
            value -= 64;
            itemStack.setAmount(64);
            player.getInventory().addItem(itemStack);
            giveItem(player, value, itemStack);
        } else {
            itemStack.setAmount((int) value);
            player.getInventory().addItem(itemStack);
        }
    }
}
