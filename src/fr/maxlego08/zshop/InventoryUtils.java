package fr.maxlego08.zshop;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.entity.Player;

public class InventoryUtils {

    /**
     * Retire un ItemStack spécifique d'un inventaire, y compris la main secondaire si c'est un PlayerInventory.
     *
     * @param inventory   L'inventaire ciblé
     * @param itemToRemove L'ItemStack à retirer
     * @param amount      La quantité à retirer
     */
    public static void removeItem(Inventory inventory, ItemStack itemToRemove, int amount) {
        if (amount <= 0 || itemToRemove == null) return;  // Vérification basique

        // Si l'inventaire appartient à un joueur, on vérifie la main secondaire
        if (inventory instanceof PlayerInventory) {
            PlayerInventory playerInventory = (PlayerInventory) inventory;
            ItemStack offhandItem = playerInventory.getItemInOffHand();

            if (isSimilarItem(offhandItem, itemToRemove)) {
                int offhandAmount = offhandItem.getAmount();

                if (offhandAmount >= amount) {
                    offhandItem.setAmount(offhandAmount - amount);
                    return;
                } else {
                    playerInventory.setItemInOffHand(null);
                    amount -= offhandAmount;
                }
            }
        }

        // Parcours de l'inventaire (slots principaux)
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack currentItem = inventory.getItem(i);

            if (isSimilarItem(currentItem, itemToRemove)) {
                int currentAmount = currentItem.getAmount();

                if (currentAmount >= amount) {
                    currentItem.setAmount(currentAmount - amount);
                    return;
                } else {
                    inventory.setItem(i, null);
                    amount -= currentAmount;
                }
            }
        }
    }

    /**
     * Vérifie si deux ItemStacks sont similaires (même type, même meta).
     *
     * @param item1 Premier ItemStack
     * @param item2 Deuxième ItemStack
     * @return true si similaires, false sinon
     */
    private static boolean isSimilarItem(ItemStack item1, ItemStack item2) {
        return item1 != null && item2 != null && item1.isSimilar(item2);
    }
}
