package fr.maxlego08.shop.zshop.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.exceptions.ItemEnchantException;
import fr.maxlego08.shop.exceptions.ItemFlagException;
import fr.maxlego08.shop.zcore.utils.ZUtils;

public class ItemStackYAMLoader extends ZUtils {

	@SuppressWarnings("deprecation")
	public ItemStack load(YamlConfiguration configuration, String path) {

		int id = configuration.getInt(path + "id");
		int data = configuration.getInt(path + ".data", 0);

		Material material = getMaterial(id);

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

		List<String> enchants = configuration.getStringList(path + "enchants");

		// Permet de charger l'enchantement de l'item
		if (enchants.size() != 0) {

			for (String enchantString : enchants) {

				try {

					String[] splitEnchant = enchantString.split(",");

					if (splitEnchant.length == 1)
						throw new ItemEnchantException(
								"an error occurred while loading the enchantment " + enchantString);

					int level = 0;
					String enchant = splitEnchant[0];
					try {
						level = Integer.valueOf(splitEnchant[1]);
					} catch (NumberFormatException e) {
						throw new ItemEnchantException(
								"an error occurred while loading the enchantment " + enchantString);
					}

					Enchantment enchantment = Enchantment.getByName(enchant);
					if (enchantment == null)
						throw new ItemEnchantException(
								"an error occurred while loading the enchantment " + enchantString);

					meta.addEnchant(enchantment, level, true);

				} catch (ItemEnchantException e) {
					e.printStackTrace();
				}

			}
		}

		List<String> flags = configuration.getStringList(path + "flags");

		// Permet de charger les différents flags
		if (flags.size() != 0) {

			for (String flagString : flags) {

				try {

					ItemFlag flag = getFlag(flagString);

					if (flag == null)
						throw new ItemFlagException("an error occurred while loading the flag " + flagString);

					meta.addItemFlags(flag);

				} catch (ItemFlagException e) {
					// TODO: handle exception
				}

			}
		}

		item.setItemMeta(meta);

		return item;

	}

	@SuppressWarnings("deprecation")
	public void save(ItemStack item, YamlConfiguration configuration, String path) {
		configuration.set(path + "id", item.getType().getId());
		configuration.set(path + "data", item.getData().getData());
		ItemMeta meta = item.getItemMeta();
		if (meta.hasDisplayName())
			configuration.set(path + "name", meta.getDisplayName());
		if (meta.hasLore())
			configuration.set(path + "lore", meta.getLore());

	}

}
