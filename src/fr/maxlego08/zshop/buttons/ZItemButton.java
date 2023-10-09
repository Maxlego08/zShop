package fr.maxlego08.zshop.buttons;

import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.ZShopManager;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.api.limit.Limit;
import fr.maxlego08.zshop.placeholder.Placeholder;
import fr.maxlego08.zshop.zcore.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZItemButton extends ZButton implements ItemButton {

    private final ShopManager shopManager;
    private final ShopPlugin plugin;
    private final double sellPrice;
    private final double buyPrice;
    private final int maxStack;
    private final List<String> lore;
    private final ShopEconomy shopEconomy;
    private final List<String> buyCommands;
    private final List<String> sellCommands;
    private final boolean giveItem;
    private final Limit serverSellLimit;
    private final Limit serverBuyLimit;

    public ZItemButton(ShopPlugin plugin, double sellPrice, double buyPrice, int maxStack, List<String> lore, ShopEconomy shopEconomy, List<String> buyCommands, List<String> sellCommands, boolean giveItem, Limit serverSellLimit, Limit serverBuyLimit) {
        this.plugin = plugin;
        this.shopManager = plugin.getShopManager();
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.maxStack = maxStack;
        this.lore = lore;
        this.shopEconomy = shopEconomy;
        this.buyCommands = buyCommands;
        this.sellCommands = sellCommands;
        this.giveItem = giveItem;
        this.serverSellLimit = serverSellLimit;
        this.serverBuyLimit = serverBuyLimit;
    }

    @Override
    public double getSellPrice() {
        return this.sellPrice;
    }

    @Override
    public double getBuyPrice() {
        return this.buyPrice;
    }

    @Override
    public double getSellPrice(int amount) {
        return amount * getUnitSellPrice();
    }

    @Override
    public double getBuyPrice(int amount) {
        return amount * getUnitBuyPrice();
    }

    @Override
    public double getUnitSellPrice() {
        return getSellPrice() / Double.parseDouble(this.getItemStack().getAmount());
    }

    @Override
    public double getUnitBuyPrice() {
        return getBuyPrice() / Double.parseDouble(this.getItemStack().getAmount());
    }

    @Override
    public boolean canSell() {
        return this.sellPrice > 0;
    }

    @Override
    public boolean canBuy() {
        return this.buyPrice > 0;
    }

    @Override
    public int getMaxStack() {
        return this.maxStack;
    }

    @Override
    public ShopEconomy getEconomy() {
        return this.shopEconomy;
    }

    @Override
    public List<String> getLore() {
        return this.lore;
    }

    @Override
    public String getSellPriceFormat(Player player, int amount) {
        return this.canSell() ? this.shopEconomy.format(this.shopManager.transformPrice(getSellPrice(amount))) : Message.CANT_SELL.msg();
    }

    @Override
    public String getBuyPriceFormat(Player player, int amount) {
        return this.canBuy() ? this.shopEconomy.format(this.shopManager.transformPrice(getBuyPrice(amount))) : Message.CANT_BUY.msg();
    }

    @Override
    public void buy(Player player, int amount) {

        ZShopManager manager = (ZShopManager) this.plugin.getShopManager();
        double currentPrice = this.getBuyPrice(amount);

        if (currentPrice < 0) return;

        // TODO REWORK LE SYSTEM D'ENVOYÉ DE MESSAGE POUR UTILISER LE SYSTEM DE META DE ZMENU

        // Money
        if (!this.shopEconomy.hasMoney(player, currentPrice)) {
            manager.message(this.plugin, player, manager.color(this.shopEconomy.getDenyMessage()));
            return;
        }

        // Inventory
        if (manager.hasInventoryFull(player)) {
            manager.message(this.plugin, player, Message.NOT_ENOUGH_PLACE);
            return;
        }

        // Limit
        if (serverBuyLimit != null) {
            int newAmount = serverBuyLimit.getAmount() + amount;
            if (newAmount > serverBuyLimit.getLimit()) {
                manager.message(this.plugin, player, Message.LIMIT_SERVER_BUY);
                return;
            }

            // NE PAS OUBLIER LA GESTION DE LA LIMIT AVEC LEVENT

            serverBuyLimit.setAmount(newAmount);
        }

        /*ZShopBuyEvent event = new ZShopBuyEvent(this, player, amount, currentPrice, economy);
        event.callEvent();

        if (event.isCancelled())
            return;*/

        this.shopEconomy.withdrawMoney(player, currentPrice);

        ItemStack itemStack = super.getItemStack().build(player).clone();
        itemStack.setAmount(amount);

        if (this.giveItem) {
            manager.give(player, itemStack);
        }

        String itemName = manager.getItemName(itemStack);
        String buyPrice = getBuyPriceFormat(player, amount);
        manager.message(this.plugin, player, Message.BUY_ITEM, "%amount%", String.valueOf(amount), "%item%", itemName, "%price%", buyPrice);

        for (String command : this.buyCommands) {
            command = command.replace("%amount%", String.valueOf(amount));
            command = command.replace("%item%", itemName);
            command = command.replace("%price%", buyPrice);
            command = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholder.getPlaceholder().setPlaceholders(player, command));
        }


        /*if (this.log) {

            String logMessage = Message.BUY_LOG.getMessage();

            logMessage = logMessage.replace("%amount%", String.valueOf(amount));
            logMessage = logMessage.replace("%item%", getItemName(itemStack));
            logMessage = logMessage.replace("%price%", format(currentPrice));
            logMessage = logMessage.replace("%currency%", this.economy.getCurrenry());
            logMessage = logMessage.replace("%player%", player.getName());

            History history = new ZHistory(HistoryType.BUY, papi(logMessage, player));
            this.historyManager.asyncValue(player.getUniqueId(), history);

        }*/

    }

    @Override
    public void sell(Player player, int amount) {
        ZShopManager manager = (ZShopManager) this.plugin.getShopManager();

        ItemStack itemStack = getItemStack().build(player);
        int item = 0;

        for (int a = 0; a != 36; a++) {
            ItemStack is = player.getInventory().getContents()[a];
            if (is != null && is.isSimilar(itemStack)) {
                item += is.getAmount();
            }
        }

        if (item <= 0) {
            manager.message(this.plugin, player, Message.NOT_ITEMS);
            return;
        }

        if (item < amount) {
            manager.message(this.plugin, player, Message.NOT_ENOUGH_ITEMS);
            return;
        }

        item = amount == 0 ? item : amount;
        int realAmount = item;

        // Limit
        if (serverSellLimit != null) {
            int newAmount = serverSellLimit.getAmount() + realAmount;
            if (newAmount > serverSellLimit.getLimit()) {
                manager.message(this.plugin, player, Message.LIMIT_SERVER_SELL);
                return;
            }

            // NE PAS OUBLIER LA GESTION DE LA LIMIT AVEC LEVENT

            serverSellLimit.setAmount(newAmount);
        }

        double currentPrice = this.getSellPrice(realAmount);

        int slot = 0;

        /*
        ZShopSellEvent event = new ZShopSellEvent(this, player, realAmount, currentPrice, economy);
        event.callEvent();

        if (event.isCancelled())
            return;

        currentPrice = event.getPrice();
        Economy economy = event.getEconomy();
        realAmount = event.getAmount();*/

        // On retire ensuite les items de l'inventaire du joueur
        for (ItemStack is : player.getInventory().getContents()) {

            if (is != null && is.isSimilar(itemStack) && item > 0) {

                int currentAmount = is.getAmount() - item;
                item -= is.getAmount();

                if (currentAmount <= 0) {
                    if (slot == 40) player.getInventory().setItemInOffHand(null);
                    else player.getInventory().removeItem(is);
                } else is.setAmount(currentAmount);
            }
            slot++;
        }

        player.updateInventory();

        this.shopEconomy.depositMoney(player, currentPrice);

        String itemName = manager.getItemName(itemStack);
        String sellPrice = getSellPriceFormat(player, realAmount);
        manager.message(this.plugin, player, Message.SELL_ITEM, "%amount%", String.valueOf(realAmount), "%item%", itemName, "%price%", sellPrice);

        for (String command : sellCommands) {
            command = command.replace("%amount%", String.valueOf(realAmount));
            command = command.replace("%item%", itemName);
            command = command.replace("%price%", sellPrice);
            command = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholder.getPlaceholder().setPlaceholders(player, command));
        }

        /*if (log) {

            String logMessage = Message.SELL_LOG.getMessage();

            logMessage = logMessage.replace("%amount%", String.valueOf(amount));
            logMessage = logMessage.replace("%item%", getItemName(itemStack));
            logMessage = logMessage.replace("%price%", format(currentPrice));
            logMessage = logMessage.replace("%currency%", this.economy.getCurrenry());
            logMessage = logMessage.replace("%player%", player.getName());

            History history = new ZHistory(HistoryType.SELL, logMessage);
            this.historyManager.asyncValue(player.getUniqueId(), history);

        }*/


    }

    @Override
    public List<String> getBuyCommands() {
        return this.buyCommands;
    }

    @Override
    public List<String> getSellCommands() {
        return this.sellCommands;
    }

    @Override
    public boolean giveItem() {
        return this.giveItem;
    }

    @Override
    public Optional<Limit> getServerBuyLimit() {
        return Optional.ofNullable(this.serverBuyLimit);
    }

    @Override
    public Optional<Limit> getServerSellLimit() {
        return Optional.ofNullable(this.serverSellLimit);
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        ItemStack itemStack = super.getCustomItemStack(player);
        ItemMeta itemMeta = itemStack.getItemMeta();

        MetaUpdater metaUpdater = plugin.getIManager().getMeta();
        metaUpdater.updateLore(itemMeta, buildLore(player), player);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void onRightClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot) {
        super.onRightClick(player, event, inventory, slot);
        if (canSell()) {
            this.plugin.getShopManager().openSell(player, this);
        }
    }

    @Override
    public void onMiddleClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot) {
        super.onMiddleClick(player, event, inventory, slot);
        if (canSell()) {
            sell(player, 0);
        }
    }

    @Override
    public void onLeftClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot) {
        super.onLeftClick(player, event, inventory, slot);
        if (canBuy()) {
            this.plugin.getShopManager().openBuy(player, this);
        }
    }

    @Override
    public List<String> buildLore(Player player) {
        ItemStack itemStack = super.getCustomItemStack(player);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> itemLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        String sellPrice = getSellPriceFormat(player, itemStack.getAmount());
        String buyPrice = getBuyPriceFormat(player, itemStack.getAmount());

        this.lore.forEach(line -> {

            line = line.replace("%sellPrice%", sellPrice);
            line = line.replace("%buyPrice%", buyPrice);

            line = line.replace("%serverSellLimit%", serverSellLimit == null ? "-1" : String.valueOf(serverSellLimit.getLimit()));
            line = line.replace("%serverBuyLimit%", serverBuyLimit == null ? "-1" : String.valueOf(serverBuyLimit.getLimit()));

            line = line.replace("%serverSellAmount%", serverSellLimit == null ? "0" : String.valueOf(serverSellLimit.getAmount()));
            line = line.replace("%serverBuyAmount%", serverBuyLimit == null ? "0" : String.valueOf(serverBuyLimit.getAmount()));

            itemLore.add(line);
        });
        return itemLore;
    }
}
