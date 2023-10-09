package fr.maxlego08.zshop;

import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.command.CommandManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.placeholder.LocalPlaceholder;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.zcore.enums.Message;
import fr.maxlego08.zshop.zcore.utils.ZUtils;
import fr.maxlego08.zshop.zcore.utils.nms.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ZShopManager extends ZUtils implements ShopManager {

    private final ShopPlugin plugin;
    private final Map<UUID, PlayerCache> cachePlayers = new HashMap<>();
    private List<String> defaultLore = new ArrayList<>();

    public ZShopManager(ShopPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void loadConfig() {
        this.defaultLore = this.plugin.getConfig().getStringList("defaultLore");

        this.loadPatterns();
        this.loadInventories();
        this.loadCommands();
    }

    /**
     * Register default inventories files
     */
    private void registerDefaultFiles() {
        List<String> files = new ArrayList<>();
        files.add("inventories/main_shop.yml");
        files.add("inventories/armors.yml");
        files.add("inventories/blocks.yml");
        files.add("inventories/farms.yml");
        files.add("inventories/mics.yml");
        files.add("inventories/mobs.yml");
        files.add("inventories/ores.yml");
        files.add("inventories/redstones.yml");
        files.add("inventories/shop_buy.yml");
        files.add("inventories/shop_sell.yml");

        files.forEach(e -> {
            if (!new File(this.plugin.getDataFolder(), e).exists()) {

                if (NMSUtils.isNewVersion()) {
                    plugin.saveResource(e.replace("inventories/", "inventories/1_13/"), e, false);
                } else {
                    plugin.saveResource(e, false);
                }
            }
        });
    }

    /**
     * Register default patterns files
     */
    private void registerDefaultPatterns() {
        List<String> files = new ArrayList<>();
        files.add("patterns/decoration.yml");
        files.add("patterns/return_home.yml");
        files.add("patterns/pagination.yml");
        files.add("patterns/back.yml");
        files.add("patterns/choose_amount.yml");

        files.forEach(e -> {
            if (!new File(this.plugin.getDataFolder(), e).exists()) {

                if (NMSUtils.isNewVersion()) {
                    plugin.saveResource(e.replace("patterns/", "patterns/1_13/"), e, false);
                } else {
                    plugin.saveResource(e, false);
                }
            }
        });
    }

    @Override
    public void registerPlaceholders() {
        LocalPlaceholder localPlaceholder = LocalPlaceholder.getInstance();
        localPlaceholder.register("item_max", (player, args) -> {
            PlayerCache playerCache = getCache(player);
            ItemButton itemButton = playerCache.getItemButton();
            return itemButton == null ? "0" : String.valueOf(itemButton.getMaxStack());
        });
    }

    @Override
    public void loadInventories() {

        // Check if file exist
        File folder = new File(this.plugin.getDataFolder(), "inventories");
        if (!folder.exists()) {
            folder.mkdir();
            registerDefaultFiles();
        }

        InventoryManager inventoryManager = this.plugin.getIManager();
        inventoryManager.deleteInventories(this.plugin);

        files(folder, file -> {
            try {
                inventoryManager.loadInventory(this.plugin, file);
            } catch (InventoryException e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public void loadPatterns() {
// Check if file exist
        File folder = new File(this.plugin.getDataFolder(), "patterns");
        if (!folder.exists()) {
            folder.mkdir();
            registerDefaultPatterns();
        }

        PatternManager patternManager = this.plugin.getPatternManager();

        files(folder, file -> {
            try {
                patternManager.loadPattern(file);
            } catch (InventoryException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void files(File folder, Consumer<File> consumer) {
        try (Stream<Path> s = Files.walk(Paths.get(folder.getPath()))) {
            s.skip(1).map(Path::toFile).filter(File::isFile).filter(e -> e.getName().endsWith(".yml")).forEach(consumer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void loadCommands() {

        File file = new File(this.plugin.getDataFolder(), "commands.yml");
        if (!file.exists()) {
            this.plugin.saveResource("commands.yml", true);
        }

        CommandManager commandManager = this.plugin.getCManager();
        commandManager.unregisterCommands(this.plugin);
        commandManager.loadCommand(this.plugin, file);
    }

    @Override
    public List<String> getDefaultLore() {
        return this.defaultLore;
    }

    @Override
    public String transformPrice(double price) {

        // TODO

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(price);
    }

    @EventHandler
    public void onQuid(PlayerQuitEvent event) {
        this.cachePlayers.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void openBuy(Player player, ItemButton itemButton) {
        this.openInventory(player, itemButton, Config.buyInventoryName);
    }

    @Override
    public void openSell(Player player, ItemButton itemButton) {
        this.openInventory(player, itemButton, Config.sellInventoryName);
    }

    /**
     * Open an inventory for purchase or sell an item
     *
     * @param player        who open inventory
     * @param itemButton    current item
     * @param inventoryName inventory name (buy or sell)
     */
    private void openInventory(Player player, ItemButton itemButton, String inventoryName) {
        PlayerCache playerCache = getCache(player);
        playerCache.setItemButton(itemButton);
        playerCache.setItemAmount(itemButton.getItemStack().parseAmount(player));

        InventoryManager inventoryManager = this.plugin.getIManager();
        List<Inventory> inventories = new ArrayList<>();
        inventoryManager.getCurrentPlayerInventory(player).ifPresent(inventories::add);
        Optional<Inventory> optional = inventoryManager.getInventory(this.plugin, inventoryName);

        if (optional.isPresent()) inventoryManager.openInventory(player, optional.get(), 1, inventories);
        else message(this.plugin, player, Message.INVENTORY_NOT_FOUND, "%name%", inventoryName);
    }

    @Override
    public PlayerCache getCache(Player player) {
        if (this.cachePlayers.containsKey(player.getUniqueId())) {
            return this.cachePlayers.get(player.getUniqueId());
        }
        PlayerCache playerCache = new ZPlayerCache();
        this.cachePlayers.put(player.getUniqueId(), playerCache);
        return playerCache;
    }
}
