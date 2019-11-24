package fr.maxlego08.shop.zshop.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.zcore.utils.ZUtils;

public class ItemStackYAMLoader extends ZUtils {

	public ItemStack load(YamlConfiguration configuration, String path) {

		int id = configuration.getInt(path + "id");
		int data = configuration.getInt(path + ".data", 0);

		Material material = getMaterial(id);

		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(material, 1, (byte) data);

		ItemMeta meta = item.getItemMeta();

		List<String> tmpLore = configuration.getStringList(path + "lore");
		if (tmpLore.size() != 0) {
			List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
			lore.addAll(color(tmpLore));
			meta.setLore(lore);
		}

		String displayName = configuration.getString(path + "name", null);
		if (displayName != null)
			meta.setDisplayName(color(displayName));

		item.setItemMeta(meta);

		return item;

	}

	@SuppressWarnings("deprecation")
	public void save(ItemStack item, YamlConfiguration configuration, String path){
		configuration.set(path+"id", item.getType().getId());
		configuration.set(path+"data", item.getData().getData());
		ItemMeta meta = item.getItemMeta();
		if (meta.hasDisplayName())
			configuration.set(path+"name", meta.getDisplayName());
		if (meta.hasLore())
			configuration.set(path+"lore", meta.getLore());
			
	}
	
}
