package fr.maxlego08.zshop;

import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.command.CommandManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.menu.button.loader.PluginLoader;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.api.economy.EconomyManager;
import fr.maxlego08.zshop.api.limit.LimiterManager;
import fr.maxlego08.zshop.buttons.ZConfirmBuyButton;
import fr.maxlego08.zshop.buttons.ZConfirmSellButton;
import fr.maxlego08.zshop.command.commands.CommandShop;
import fr.maxlego08.zshop.economy.ZEconomyManager;
import fr.maxlego08.zshop.limit.ZLimitManager;
import fr.maxlego08.zshop.loader.AddButtonLoader;
import fr.maxlego08.zshop.loader.ItemButtonLoader;
import fr.maxlego08.zshop.loader.RemoveButtonLoader;
import fr.maxlego08.zshop.loader.ShowItemButtonLoader;
import fr.maxlego08.zshop.placeholder.LocalPlaceholder;
import fr.maxlego08.zshop.placeholder.Placeholder;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.save.MessageLoader;
import fr.maxlego08.zshop.zcore.ZPlugin;
import fr.maxlego08.zshop.zcore.utils.plugins.Metrics;
import fr.maxlego08.zshop.zcore.utils.plugins.Plugins;
import org.bukkit.Bukkit;
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
    private ZLimitManager limitManager = new ZLimitManager(this);
    private InventoryManager inventoryManager;
    private CommandManager commandManager;
    private PatternManager patternManager;
    private ButtonManager buttonManager;

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zshop");

        this.preEnable();

        if (isEnable(Plugins.PLACEHOLDER)) Placeholder.getPlaceholder();

        ServicesManager manager = this.getServer().getServicesManager();
        manager.register(EconomyManager.class, economyManager, this, ServicePriority.Normal);
        manager.register(ShopManager.class, shopManager, this, ServicePriority.Normal);

        this.inventoryManager = this.getProvider(InventoryManager.class);
        this.commandManager = this.getProvider(CommandManager.class);
        this.patternManager = this.getProvider(PatternManager.class);
        this.buttonManager = this.getProvider(ButtonManager.class);

        this.registerCommand("zshoplugin", new CommandShop(this), "zpl");

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));

        Bukkit.getPluginManager().registerEvents(this.limitManager, this);
        this.addListener(this.shopManager);

        // Load limiter before
        this.limitManager.load(this.getPersist());

        this.saveDefaultConfig();
        this.loadButtons();
        this.loadFiles();
        this.economyManager.loadEconomies();
        this.shopManager.loadConfig();

        new Metrics(this, 5881);

        this.limitManager.registerPlaceholders();
        this.shopManager.registerPlaceholders();

        this.limitManager.deletes();
        this.limitManager.verifyPlayersLimit();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.limitManager.save(this.getPersist());
        this.saveFiles();

        this.postDisable();
    }

    @Override
    public void reloadFiles() {

        this.limitManager.invalid();

        super.reloadFiles();
        this.loadButtons();
        this.reloadConfig();
        this.economyManager.loadEconomies();
        this.shopManager.loadConfig();

        this.limitManager.deletes();
        this.limitManager.verifyPlayersLimit();

        // Ajouter la cloture des inventaires
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

    private void loadButtons() {

        this.buttonManager.unregisters(this);
        this.buttonManager.register(new ItemButtonLoader(this));
        this.buttonManager.register(new ShowItemButtonLoader(this));
        this.buttonManager.register(new AddButtonLoader(this));
        this.buttonManager.register(new RemoveButtonLoader(this));
        this.buttonManager.register(new PluginLoader(this, ZConfirmBuyButton.class, "zshop_confirm_buy"));
        this.buttonManager.register(new PluginLoader(this, ZConfirmSellButton.class, "zshop_confirm_sell"));

    }

    public LimiterManager getLimiterManager() {
        return limitManager;
    }
}
