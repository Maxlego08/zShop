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
			lore.addAll(tmpLore);
			meta.setLore(lore);
		}

		String displayName = configuration.getString(path + "name", null);
		if (displayName != null)
			meta.setDisplayName(displayName);

		item.setItemMeta(meta);

		return item;

	}

}
