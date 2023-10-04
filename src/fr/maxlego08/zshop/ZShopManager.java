package fr.maxlego08.zshop;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.command.CommandManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.zcore.utils.nms.NMSUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ZShopManager implements ShopManager {

    private final ShopPlugin plugin;

    public ZShopManager(ShopPlugin plugin) {
        this.plugin = plugin;
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
}
