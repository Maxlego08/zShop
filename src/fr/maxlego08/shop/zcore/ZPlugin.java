package fr.maxlego08.shop.zcore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.inventory.InventoryManager;
import fr.maxlego08.shop.listener.ListenerAdapter;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.ItemDecoder;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;
import fr.maxlego08.shop.zcore.utils.plugins.Plugins;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zcore.utils.storage.Saveable;
import net.milkbowl.vault.economy.Economy;

public abstract class ZPlugin extends JavaPlugin {

	private final Logger log = new Logger(this.getDescription().getFullName());
	private Gson gson;
	private Persist persist;
	private long enableTime;
	private List<Saveable> savers = new ArrayList<>();
	private List<ListenerAdapter> listenerAdapters = new ArrayList<>();
	private Economy economy = null;
	protected boolean isLoaded = false;

	protected CommandManager commandManager;
	protected InventoryManager inventoryManager;

	private PlayerPoints playerPoints;
	private PlayerPointsAPI playerPointsAPI;

	protected boolean preEnable() {

		enableTime = System.currentTimeMillis();

		log.log("=== ENABLE START ===");
		log.log("Plugin Version V<&>c" + getDescription().getVersion(), LogType.INFO);

		getDataFolder().mkdirs();

		gson = getGsonBuilder().create();
		persist = new Persist(this);

		if (getPlugin(Plugins.VAULT) != null)
			economy = getProvider(Economy.class);

		if (economy == null && isEnable(Plugins.VAULT)) {
			Logger.info("Vault error, retry in 5 secondes...");
			Bukkit.getScheduler().runTaskLater(this, () -> {
				economy = getProvider(Economy.class);
				if (economy != null)
					Logger.info("Vault loaded !");
				else
					Logger.info("Vault error :'( don't cry", LogType.ERROR);
			}, 20 * 5);
		}

		saveDefaultConfig();

		this.hookPlayerPoints();

		List<String> files = Arrays.asList("blocks", "ores", "miscellaneous", "mobs", "farm", "redstone", "foods",
				"shop", "sell", "buy", "confirm", "menu");

		boolean isNew = ItemDecoder.isNewVersion();
		for (String file : files) {

			if (isNew) {
				if (!new File(getDataFolder() + "/inventories/" + file + ".yml").exists())
					saveResource("inventories/1_13/" + file + ".yml", "inventories/" + file + ".yml", false);
			} else {
				if (!new File(getDataFolder() + "/inventories/" + file + ".yml").exists())
					saveResource("inventories/" + file + ".yml", false);
			}
		}
		return true;

	}

	/**
	 * @return boolean
	 */
	protected void hookPlayerPoints() {
		try {
			final Plugin plugin = (Plugin) this.getServer().getPluginManager().getPlugin("PlayerPoints");
			if (plugin == null)
				return;
			playerPoints = PlayerPoints.class.cast(plugin);
			if (playerPoints != null) {
				playerPointsAPI = playerPoints.getAPI();
				log.log("PlayerPoint plugin detection performed successfully", LogType.SUCCESS);
			} else
				log.log("Impossible de charger player point !", LogType.SUCCESS);
		} catch (Exception e) {
		}
	}

	protected void postEnable() {

		if (inventoryManager != null)
			inventoryManager.sendLog();

		if (commandManager != null)
			commandManager.registerCommands();

		isLoaded = true;

		log.log("=== ENABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	protected void preDisable() {

		enableTime = System.currentTimeMillis();
		log.log("=== DISABLE START ===");

	}

	protected void postDisable() {

		log.log("=== DISABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	/**
	 * Build gson
	 * 
	 * @return
	 */
	public GsonBuilder getGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE);
	}

	/**
	 * Add a listener
	 * 
	 * @param listener
	 */
	public void addListener(Listener listener) {
		if (listener instanceof Saveable)
			addSave((Saveable) listener);
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	/**
	 * Add a listener from ListenerAdapter
	 * 
	 * @param adapter
	 */
	public void addListener(ListenerAdapter adapter) {
		if (adapter instanceof Saveable)
			addSave((Saveable) adapter);
		listenerAdapters.add(adapter);
	}

	/**
	 * Add a Saveable
	 * 
	 * @param saver
	 */
	public void addSave(Saveable saver) {
		this.savers.add(saver);
	}

	/**
	 * Get logger
	 * 
	 * @return loggers
	 */
	public Logger getLog() {
		return this.log;
	}

	/**
	 * Get gson
	 * 
	 * @return {@link Gson}
	 */
	public Gson getGson() {
		return gson;
	}

	public Persist getPersist() {
		return persist;
	}

	/**
	 * Get all saveables
	 * 
	 * @return savers
	 */
	public List<Saveable> getSavers() {
		return savers;
	}

	/**
	 * 
	 * @param classz
	 * @return
	 */
	protected <T> T getProvider(Class<T> classz) {
		RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
		if (provider == null) {
			log.log("Unable to retrieve the provider " + classz.toString(), LogType.WARNING);
			return null;
		}
		return provider.getProvider() != null ? (T) provider.getProvider() : null;
	}

	public Economy getEconomy() {
		return economy;
	}

	/**
	 * 
	 * @return listenerAdapters
	 */
	public List<ListenerAdapter> getListenerAdapters() {
		return listenerAdapters;
	}

	/**
	 * @return the commandManager
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * @return the inventoryManager
	 */
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	/**
	 * 
	 * @param pluginName
	 * @return
	 */
	protected boolean isEnable(Plugins pl) {
		Plugin plugin = getPlugin(pl);
		return plugin == null ? false : plugin.isEnabled();
	}

	/**
	 * 
	 * @param pluginName
	 * @return
	 */
	protected Plugin getPlugin(Plugins plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin.getName());
	}

	/**
	 * Register Inventory
	 * 
	 * @param inventory
	 * @param vInventory
	 */
	protected void registerInventory(EnumInventory inventory, VInventory vInventory) {
		inventoryManager.addInventory(inventory, vInventory);
	}

	public PlayerPoints getPlayerPoints() {
		return playerPoints;
	}

	public PlayerPointsAPI getPlayerPointsAPI() {
		return playerPointsAPI;
	}

	public void saveResource(String resourcePath, String toPath, boolean replace) {
		if (resourcePath != null && !resourcePath.equals("")) {
			resourcePath = resourcePath.replace('\\', '/');
			InputStream in = this.getResource(resourcePath);
			if (in == null) {
				throw new IllegalArgumentException(
						"The embedded resource '" + resourcePath + "' cannot be found in " + this.getFile());
			} else {
				File outFile = new File(getDataFolder(), toPath);
				int lastIndex = toPath.lastIndexOf(47);
				File outDir = new File(getDataFolder(), toPath.substring(0, lastIndex >= 0 ? lastIndex : 0));
				if (!outDir.exists()) {
					outDir.mkdirs();
				}

				try {
					if (outFile.exists() && !replace) {
						getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile
								+ " because " + outFile.getName() + " already exists.");
					} else {
						OutputStream out = new FileOutputStream(outFile);
						byte[] buf = new byte[1024];

						int len;
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						out.close();
						in.close();
					}
				} catch (IOException var10) {
					getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
				}

			}
		} else {
			throw new IllegalArgumentException("ResourcePath cannot be null or empty");
		}
	}

}
