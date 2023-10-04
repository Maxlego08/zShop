package fr.maxlego08.zshop;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.command.CommandManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.api.economy.EconomyManager;
import fr.maxlego08.zshop.command.commands.CommandShop;
import fr.maxlego08.zshop.economy.ZEconomyManager;
import fr.maxlego08.zshop.placeholder.LocalPlaceholder;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.save.MessageLoader;
import fr.maxlego08.zshop.zcore.ZPlugin;
import fr.maxlego08.zshop.zcore.utils.plugins.Metrics;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class ShopPlugin extends ZPlugin {

    private final EconomyManager economyManager = new ZEconomyManager(this);
    private final ShopManager shopManager = new ZShopManager(this);
    private InventoryManager inventoryManager;
    private CommandManager commandManager;
    private PatternManager patternManager;

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zshop");

        this.preEnable();

        ServicesManager manager = this.getServer().getServicesManager();
        manager.register(EconomyManager.class, economyManager, this, ServicePriority.Normal);
        manager.register(ShopManager.class, shopManager, this, ServicePriority.Normal);

        this.inventoryManager = this.getProvider(InventoryManager.class);
        this.commandManager = this.getProvider(CommandManager.class);
        this.patternManager = this.getProvider(PatternManager.class);

        this.registerCommand("zshoplugin", new CommandShop(this), "zpl");

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));

        this.loadFiles();
        this.economyManager.loadEconomies();
        this.shopManager.loadPatterns();
        this.shopManager.loadInventories();
        this.shopManager.loadCommands();

        new Metrics(this, 5881);

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

    @Override
    public void reloadFiles() {
        super.reloadFiles();
        this.economyManager.loadEconomies();
        this.shopManager.loadPatterns();
        this.shopManager.loadInventories();
        this.shopManager.loadCommands();
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public InventoryManager getIManager() {
        return inventoryManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public CommandManager getCManager() {
        return commandManager;
    }

    public PatternManager getPatternManager() {
        return patternManager;
    }
}
