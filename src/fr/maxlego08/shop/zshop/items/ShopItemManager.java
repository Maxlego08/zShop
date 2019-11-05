package fr.maxlego08.shop.zshop.items;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.exceptions.ItemCreateException;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zshop.factories.Items;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;
import fr.maxlego08.shop.zshop.utils.ItemStackYAMLoader;

public class ShopItemManager implements Items {

	private final ZShop plugin;

	private Map<Integer, List<ShopItem>> items = new HashMap<Integer, List<ShopItem>>();

	public ShopItemManager(ZShop plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public List<ShopItem> getItems(int category) {
		return items.getOrDefault(category, new ArrayList<>());
	}

	@Override
	public void load() {
		File file = new File(plugin.getDataFolder() + File.separator + "items.yml");
		if (!file.exists())
			return;
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

		if (configuration.getString("items") == null) {
			Logger.info("Impossible de charger les items !", LogType.ERROR);
			return;
		}

		ItemStackYAMLoader itemStackYAMLoader = new ItemStackYAMLoader();
		
		//Chargement des categories
		for (String categoryId : configuration.getConfigurationSection("items.").getKeys(false)) {

			List<ShopItem> currentItems = new ArrayList<>();
			int currentId = 0;
			
			try{
				currentId = Integer.valueOf(categoryId);
			}catch (NumberFormatException e) {
				Logger.info("La catégorie " + categoryId +" n'est pas un nombre !", LogType.ERROR);
				return;
			}
			
			//Chargement des items
			for (String itemId : configuration.getConfigurationSection("items." + categoryId + ".items.")
					.getKeys(false)) {

				try{
					
					//Chargement des éléments de l'item
					
					String currentPath = "items." + categoryId + ".items."+itemId+".";
					
					ShopType type = ShopType.valueOf(configuration.getString(currentPath+"type").toUpperCase());
					ItemStack itemStack = itemStackYAMLoader.load(configuration, currentPath+".item.");
					
					double sellPrice = configuration.getDouble(currentPath+"sellPrice", 0);
					double buyPrice = configuration.getDouble(currentPath+"buyPrice", 0);
					int maxStackSize = configuration.getInt(currentPath+"item.stack", 64);
					
					ShopItem item = null;
					switch (type) {
					case ITEM:{
						item = new ShopItemConsomable(currentId, itemStack, sellPrice, buyPrice, maxStackSize);
						break;
					}
					case UNIQUE_ITEM:{
						break;
					}
					}

					currentItems.add(item);
					
				}catch (Exception e) {
					
					//S'il y a une erreur alors il est impossible de charger l'item
					
					try {
						throw new ItemCreateException("Could load item with id " + itemId + " in category " + categoryId);
					} catch (ItemCreateException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			//Ajout des items
			
			this.items.put(currentId, currentItems);
		}
		
		Logger.info(file.getAbsolutePath()+" loaded successfully !", LogType.SUCCESS);
	}

}
