package fr.maxlego08.zshop.buttons;

import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.ZShopManager;
import fr.maxlego08.zshop.api.PriceModifier;
import fr.maxlego08.zshop.api.PriceType;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.api.economy.ShopEconomy;
import fr.maxlego08.zshop.api.event.events.ZShopBuyEvent;
import fr.maxlego08.zshop.api.event.events.ZShopSellEvent;
import fr.maxlego08.zshop.api.limit.Limit;
import fr.maxlego08.zshop.api.limit.LimiterManager;
import fr.maxlego08.zshop.api.limit.PlayerLimit;
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
import java.util.concurrent.atomic.AtomicReference;

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
    private final Limit playerSellLimit;
    private final Limit playerBuyLimit;

    public ZItemButton(ShopPlugin plugin, double sellPrice, double buyPrice, int maxStack, List<String> lore, ShopEconomy shopEconomy, List<String> buyCommands, List<String> sellCommands, boolean giveItem, Limit serverSellLimit, Limit serverBuyLimit, Limit playerSellLimit, Limit playerBuyLimit) {
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
        this.playerSellLimit = playerSellLimit;
        this.playerBuyLimit = playerBuyLimit;
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
        if (!this.canSell()) return Message.CANT_SELL.msg();
        AtomicReference<Double> price = new AtomicReference<>(getSellPrice(amount));
        Optional<PriceModifier> optional = this.shopManager.getPriceModifier(player, PriceType.SELL);
        optional.ifPresent(modifier -> price.updateAndGet(v -> v * modifier.getModifier()));
        return this.shopEconomy.format(this.shopManager.transformPrice(price.get()), price.get());
    }

    @Override
    public String getBuyPriceFormat(Player player, int amount) {
        if (!this.canBuy()) return Message.CANT_BUY.msg();
        AtomicReference<Double> price = new AtomicReference<>(getBuyPrice(amount));
        Optional<PriceModifier> optional = this.shopManager.getPriceModifier(player, PriceType.BUY);
        optional.ifPresent(modifier -> price.updateAndGet(v -> v * modifier.getModifier()));
        return this.shopEconomy.format(this.shopManager.transformPrice(price.get()), price.get());
    }

    @Override
    public String getSellPriceFormat(int amount) {
        double sellPrice = getSellPrice(amount);
        return this.canSell() ? this.shopEconomy.format(this.shopManager.transformPrice(sellPrice), sellPrice) : Message.CANT_SELL.msg();
    }

    @Override
    public String getBuyPriceFormat(int amount) {
        double buyPrice = getBuyPrice(amount);
        return this.canBuy() ? this.shopEconomy.format(this.shopManager.transformPrice(buyPrice), buyPrice) : Message.CANT_BUY.msg();
    }

    @Override
    public void buy(Player player, int amount) {

        ZShopManager manager = (ZShopManager) this.plugin.getShopManager();
        double currentPrice = this.getBuyPrice(amount);

        /* If the price is invalid, then we stop */
        if (currentPrice < 0) return;

        /* ECONOMY CHECK */
        if (!this.shopEconomy.hasMoney(player, currentPrice)) {
            manager.message(this.plugin, player, manager.color(this.shopEconomy.getDenyMessage()));
            return;
        }
        /* END ECONOMY CHECK */

        /* INVENTORY SLOT CHECK */
        if (manager.hasInventoryFull(player)) {
            manager.message(this.plugin, player, Message.NOT_ENOUGH_PLACE);
            return;
        }
        /* END INVENTORY SLOT CHECK */

        /* SERVER LIMIT */
        int newServerLimitAmount = 0;
        int newPlayerLimitAmount = 0;
        String material = this.getItemStack().getMaterial();
        LimiterManager limiterManager = this.plugin.getLimiterManager();

        if (serverBuyLimit != null) {
            newServerLimitAmount = serverBuyLimit.getAmount() + amount;
            if (newServerLimitAmount > serverBuyLimit.getLimit()) {
                manager.message(this.plugin, player, Message.LIMIT_SERVER_BUY);
                return;
            }
        }
        /* END SERVER LIMIT */

        /* PLAYER LIMIT */
        if (playerBuyLimit != null) {
            // We will recover the value without creating a new player limit
            Optional<PlayerLimit> optional = limiterManager.getLimit(player);
            newPlayerLimitAmount = optional.map(e -> e.getBuyAmount(material)).orElse(0) + amount;
            if (newPlayerLimitAmount > playerBuyLimit.getLimit()) {
                manager.message(this.plugin, player, Message.LIMIT_SERVER_SELL);
                return;
            }
        }
        /* END PLAYER LIMIT */

        /* BUKKIT EVENT */
        ZShopBuyEvent event = new ZShopBuyEvent(player, this, amount, currentPrice, newServerLimitAmount, newPlayerLimitAmount);
        event.call();

        if (event.isCancelled()) return;

        newServerLimitAmount = event.getServerLimit();
        newPlayerLimitAmount = event.getPlayerLimit();

        /* If the purchase is confirmed, then we will change the limits */
        if (serverBuyLimit != null && newServerLimitAmount > 0) serverBuyLimit.setAmount(newServerLimitAmount);
        if (newPlayerLimitAmount > 0) limiterManager.getOrCreate(player).setBuyAmount(material, newPlayerLimitAmount);

        amount = event.getAmount();
        currentPrice = event.getPrice();
        /* END BUKKIT EVENT */

        /* We withdraw the money if the price is greater than 0  */
        if (currentPrice > 0) this.shopEconomy.withdrawMoney(player, currentPrice);

        /* BUILD ITEM AND GIVE IT TO PLAYER */
        ItemStack itemStack = super.getItemStack().build(player).clone();
        itemStack.setAmount(amount);

        if (this.giveItem) manager.give(player, itemStack);
        /* END BUILD ITEM AND GIVE IT TO PLAYER */

        String itemName = manager.getItemName(itemStack);
        String buyPrice = getBuyPriceFormat(player, amount);
        manager.message(this.plugin, player, Message.BUY_ITEM, "%amount%", String.valueOf(amount), "%item%", itemName, "%price%", buyPrice);

        /* COMMANDS */
        for (String command : this.buyCommands) {
            command = command.replace("%amount%", String.valueOf(amount));
            command = command.replace("%item%", itemName);
            command = command.replace("%price%", buyPrice);
            command = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholder.getPlaceholder().setPlaceholders(player, command));
        }
        /* END COMMANDS */

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
        int items = 0;

        for (int a = 0; a != 36; a++) {
            ItemStack is = player.getInventory().getContents()[a];
            if (is != null && is.isSimilar(itemStack)) {
                items += is.getAmount();
            }
        }

        if (items <= 0) {
            manager.message(this.plugin, player, Message.NOT_ITEMS);
            return;
        }

        if (items < amount) {
            manager.message(this.plugin, player, Message.NOT_ENOUGH_ITEMS);
            return;
        }

        items = amount == 0 ? items : amount;
        int realAmount = items;

        int newServerLimitAmount = 0;
        int newPlayerLimitAmount = 0;
        String material = this.getItemStack().getMaterial();
        LimiterManager limiterManager = this.plugin.getLimiterManager();

        /* SERVER LIMIT */
        if (serverSellLimit != null) {
            newServerLimitAmount = serverSellLimit.getAmount() + realAmount;
            if (newServerLimitAmount > serverSellLimit.getLimit()) {
                manager.message(this.plugin, player, Message.LIMIT_SERVER_SELL);
                return;
            }
        }
        /* END SERVER LIMIT */

        /* PLAYER LIMIT */
        if (playerSellLimit != null) {
            Optional<PlayerLimit> optional = limiterManager.getLimit(player);
            newPlayerLimitAmount = optional.map(e -> e.getSellAmount(material)).orElse(0) + realAmount;
            if (newPlayerLimitAmount > playerSellLimit.getLimit()) {
                manager.message(this.plugin, player, Message.LIMIT_SERVER_SELL);
                return;
            }
        }
        /* END PLAYER LIMIT */

        double currentPrice = this.getSellPrice(realAmount);

        int slot = 0;

        /* BUKKIT EVENT */

        ZShopSellEvent event = new ZShopSellEvent(player, this, realAmount, currentPrice, newServerLimitAmount, newPlayerLimitAmount);
        event.call();

        if (event.isCancelled()) return;

        newServerLimitAmount = event.getServerLimit();
        newPlayerLimitAmount = event.getPlayerLimit();

        /* If the sell is confirmed, then we will change the limits */
        if (serverSellLimit != null && newServerLimitAmount > 0) serverSellLimit.setAmount(newServerLimitAmount);
        if (newPlayerLimitAmount > 0) limiterManager.getOrCreate(player).setSellAmount(material, newPlayerLimitAmount);

        realAmount = event.getAmount();
        currentPrice = event.getPrice();

        /* END BUKKIT EVENT */

        /* Items are then removed from the player’s inventory */
        for (ItemStack is : player.getInventory().getContents()) {

            if (is != null && is.isSimilar(itemStack) && items > 0) {

                int currentAmount = is.getAmount() - items;
                items -= is.getAmount();

                if (currentAmount <= 0) {
                    if (slot == 40) player.getInventory().setItemInOffHand(null);
                    else player.getInventory().removeItem(is);
                } else is.setAmount(currentAmount);
            }
            slot++;
        }
        player.updateInventory();
        /* END ITEMS */

        /* We withdraw the money if the price is greater than 0  */
        if (currentPrice > 0) this.shopEconomy.depositMoney(player, currentPrice);

        String itemName = manager.getItemName(itemStack);
        String sellPrice = getSellPriceFormat(player, realAmount);
        manager.message(this.plugin, player, Message.SELL_ITEM, "%amount%", String.valueOf(realAmount), "%item%", itemName, "%price%", sellPrice);

        /* COMMANDS */
        for (String command : sellCommands) {
            command = command.replace("%amount%", String.valueOf(realAmount));
            command = command.replace("%item%", itemName);
            command = command.replace("%price%", sellPrice);
            command = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholder.getPlaceholder().setPlaceholders(player, command));
        }
        /* END COMMANDS */

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
    public Optional<Limit> getPlayerBuyLimit() {
        return Optional.ofNullable(this.playerBuyLimit);
    }

    @Override
    public Optional<Limit> getPlayerSellLimit() {
        return Optional.ofNullable(this.playerSellLimit);
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

        String realSellPrice = getSellPriceFormat(itemStack.getAmount());
        String realBuyPrice = getBuyPriceFormat(itemStack.getAmount());

        this.lore.forEach(line -> {

            line = line.replace("%sellPrice%", sellPrice);
            line = line.replace("%buyPrice%", buyPrice);
            line = line.replace("%realSellPrice%", realSellPrice);
            line = line.replace("%realBuyPrice%", realBuyPrice);

            /* SERVER LIMIT */
            line = line.replace("%serverSellLimit%", serverSellLimit == null ? "-1" : String.valueOf(serverSellLimit.getLimit()));
            line = line.replace("%serverBuyLimit%", serverBuyLimit == null ? "-1" : String.valueOf(serverBuyLimit.getLimit()));

            line = line.replace("%serverSellAmount%", serverSellLimit == null ? "0" : String.valueOf(serverSellLimit.getAmount()));
            line = line.replace("%serverBuyAmount%", serverBuyLimit == null ? "0" : String.valueOf(serverBuyLimit.getAmount()));
            /* END SERVER LIMIT */

            /* PLAYER LIMIT */
            line = line.replace("%playerSellLimit%", playerSellLimit == null ? "-1" : String.valueOf(playerSellLimit.getLimit()));
            line = line.replace("%playerBuyLimit%", playerBuyLimit == null ? "-1" : String.valueOf(playerBuyLimit.getLimit()));

            String material = this.getItemStack().getMaterial();
            Optional<PlayerLimit> optional = this.plugin.getLimiterManager().getLimit(player);
            line = line.replace("%playerSellAmount%", optional.map(limit -> String.valueOf(limit.getSellAmount(material))).orElse("0"));
            line = line.replace("%playerBuyAmount%", optional.map(playerLimit -> String.valueOf(playerLimit.getBuyAmount(material))).orElse("0"));
            /* END PLAYER LIMIT */

            itemLore.add(line);
        });
        return itemLore;
    }
}