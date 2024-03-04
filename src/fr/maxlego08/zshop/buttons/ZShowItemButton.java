package fr.maxlego08.zshop.buttons;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import fr.maxlego08.zshop.api.buttons.ShowItemButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ZShowItemButton extends ZButton implements ShowItemButton {

    private final ShopPlugin plugin;
    private final List<String> lore;

    public ZShowItemButton(ShopPlugin plugin, List<String> lore) {
        this.plugin = plugin;
        this.lore = lore;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        PlayerCache playerCache = this.plugin.getShopManager().getCache(player);
        ItemButton itemButton = playerCache.getItemButton();
        if (itemButton == null) return super.getCustomItemStack(player);

        ItemStack itemStack = itemButton.getCustomItemStack(player);
        itemStack.setAmount(playerCache.getAmount());

        ItemMeta itemMeta = itemStack.getItemMeta();

        String sellPrice = itemButton.getSellPriceFormat(player, itemStack.getAmount());
        String buyPrice = itemButton.getBuyPriceFormat(player, itemStack.getAmount());

        List<String> itemLore = this.lore.stream().map(line -> {

            line = line.replace("%sellPrice%", sellPrice);
            line = line.replace("%buyPrice%", buyPrice);

            return line;
        }).collect(Collectors.toList());

        this.plugin.getIManager().getMeta().updateLore(itemMeta, itemLore, player);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public List<String> getLore() {
        return this.lore;
    }
}
