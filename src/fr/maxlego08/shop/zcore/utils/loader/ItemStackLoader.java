package fr.maxlego08.shop.zcore.utils.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.songoda.epicheads.core.utils.ItemUtils;
import com.songoda.epicheads.head.Head;

import fr.maxlego08.shop.api.Loader;
import fr.maxlego08.shop.api.exceptions.ItemEnchantException;
import fr.maxlego08.shop.api.exceptions.ItemFlagException;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.Heads;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.itemstack.NMSUtils;
import me.arcaniax.hdb.api.HeadDatabaseAPI;

@SuppressWarnings("deprecation")
public class ItemStackLoader extends ZUtils implements Loader<ItemStack> {

	public ItemStack load(YamlConfiguration configuration, String path, Object... args) {

		int data = configuration.getInt(path + "data", 0);
		int amount = configuration.getInt(path + "amount", 1);
		short durability = (short) configuration.getInt(path + "durability", 0);

		Material material = null;

		int value = configuration.getInt(path + "material", 0);
		if (value != 0)
			material = getMaterial(value);

		if (material == null) {
			String str = configuration.getString(path + "material", null);
			if (str == null)
				return null;
			material = Material.getMaterial(str.toUpperCase());
		}

		ItemStack item = null;

		String currentMateraial = configuration.getString(path + "material", null);

		if (currentMateraial.startsWith("hdb:")) {

			String idAsString = currentMateraial.replace("hdb:", "");
			try {
				int id = Integer.valueOf(idAsString);

				HeadDatabaseAPI api = new HeadDatabaseAPI();

				item = api.getItemHead(String.valueOf(id));

			} catch (Exception e) {
				Logger.info("Impossible to find the head with : " + idAsString, LogType.ERROR);
				e.printStackTrace();
			}
		} else if (currentMateraial.startsWith("eh:")) {

			String idAsString = currentMateraial.replace("eh:", "");

			try {
				int id = Integer.valueOf(idAsString);

				Heads heads = new Heads();
				Optional<Head> optional = heads.getHead(id);

				if (!optional.isPresent()) {
					Logger.info("Impossible to find the head with : " + idAsString, LogType.ERROR);
					item = new ItemStack(Material.AIR);
				} else {

					Head head = optional.get();
					item = ItemUtils.getCustomHead(head.getURL());

				}

			} catch (Exception e) {
				Logger.info("Impossible to find the head with : " + idAsString, LogType.ERROR);
				e.printStackTrace();
			}

		} else {

			if (material == null || material.equals(Material.AIR))
				return null;

			item = new ItemStack(material, amount, (byte) data);

		}

		if (configuration.contains(path + "url")) {

			item = createSkull(configuration.getString(path + "url"));

		} else if (configuration.contains(path + "potion")) {

			PotionType type = PotionType.valueOf(configuration.getString(path + "potion", "REGEN").toUpperCase());
			int level = configuration.getInt(path + "level", 1);
			boolean splash = configuration.getBoolean(path + "splash", false);
			boolean extended = configuration.getBoolean(path + "extended", false);

			item = new Potion(type, level, splash, extended).toItemStack(amount);

		} else if (configuration.contains(path + "color")) {

			if (material.equals(Material.LEATHER_BOOTS) || material.equals(Material.LEATHER_HELMET)
					|| material.equals(Material.LEATHER_LEGGINGS) || material.equals(Material.LEATHER_CHESTPLATE)) {

				LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();
				Color color = fromString(configuration.getString(path + "color"));
				
				if (color != null)
					armorMeta.setColor(color);
				else
					Logger.info("Impossible de trouver la couleur: \"" + color + "\"");
				
				item.setItemMeta(armorMeta);
			}

		}

		// Si après tout l'item est null alors fuck off
		if (item == null)
			return null;

		if (durability != 0)
			item.setDurability(durability);

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

		boolean isGlowing = configuration.getBoolean(path + "glow");

		if (isGlowing && NMSUtils.getNMSVersion() != 1.7) {

			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		}

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

					if (material.equals(Material.ENCHANTED_BOOK)) {

						((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment, level, true);

					} else
						meta.addEnchant(enchantment, level, true);

				} catch (ItemEnchantException e) {
					e.printStackTrace();
				}

			}
		}

		List<String> flags = configuration.getStringList(path + "flags");

		// Permet de charger les différents flags
		if (flags.size() != 0 && NMSUtils.getNMSVersion() != 1.7) {

			for (String flagString : flags) {

				try {

					ItemFlag flag = getFlag(flagString);

					if (flag == null)
						throw new ItemFlagException("an error occurred while loading the flag " + flagString);

					meta.addItemFlags(flag);

				} catch (ItemFlagException e) {
					e.printStackTrace();
				}

			}
		}

		item.setItemMeta(meta);

		return item;

	}

	public void save(ItemStack item, YamlConfiguration configuration, String path) {

		if (item == null) {
			Logger.info("Impossible de sauvegarder l'item car il est null ! Le path: " + path, LogType.ERROR);
			return;
		}

		configuration.set(path + "id", item.getType().getId());
		configuration.set(path + "data", item.getData().getData());
		configuration.set(path + "amount", item.getAmount());
		configuration.set(path + "durability", item.getDurability());
		ItemMeta meta = item.getItemMeta();
		if (meta.hasDisplayName())
			configuration.set(path + "name", meta.getDisplayName().replace("&", "§"));
		if (meta.hasLore())
			configuration.set(path + "lore", colorReverse(meta.getLore()));
		if (NMSUtils.getNMSVersion() != 1.7 && meta.getItemFlags().size() != 0)
			configuration.set(path + "flags",
					meta.getItemFlags().stream().map(flag -> flag.name()).collect(Collectors.toList()));
		if (meta.hasEnchants()) {
			List<String> enchantList = new ArrayList<>();
			meta.getEnchants().forEach((enchant, level) -> enchantList.add(enchant.getName() + "," + level));
			configuration.set(path + "enchants", enchantList);
		}

		if (meta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta) meta).hasStoredEnchants()) {
			List<String> enchantList = new ArrayList<>();
			((EnchantmentStorageMeta) meta).getStoredEnchants()
					.forEach((enchant, level) -> enchantList.add(enchant.getName() + "," + level));

			configuration.set(path + "enchants", enchantList);
		}

	}

}
