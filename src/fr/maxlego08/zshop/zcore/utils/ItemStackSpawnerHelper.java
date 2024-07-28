package fr.maxlego08.zshop.zcore.utils;

import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemStackSpawnerHelper {

    public static EntityType getEntityType(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey("zshop", ItemButton.nbtMobSpawnerKey);
        if (persistentDataContainer.has(key)) {
            try {
                return EntityType.valueOf(persistentDataContainer.get(key, PersistentDataType.STRING));
            } catch (Exception ignored) {
                return EntityType.UNKNOWN;
            }
        }
        return EntityType.UNKNOWN;
    }

    public static ItemStack setEntityType(ItemStack itemStack, String entityType) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey("zshop", ItemButton.nbtMobSpawnerKey);
        persistentDataContainer.set(key, PersistentDataType.STRING, entityType);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
