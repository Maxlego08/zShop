package fr.maxlego08.zshop.buttons;

import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.PlayerCache;
import fr.maxlego08.zshop.api.buttons.EconomyAction;
import fr.maxlego08.zshop.api.buttons.ShowConfirmItemButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ZShowConfirmItemButton extends ZButton implements ShowConfirmItemButton {

    private final ShopPlugin plugin;

    public ZShowConfirmItemButton(Plugin plugin) {
        this.plugin = (ShopPlugin) plugin;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        PlayerCache playerCache = this.plugin.getShopManager().getCache(player);
        EconomyAction itemButton = playerCache.getEconomyAction();
        if (itemButton == null) return super.getCustomItemStack(player);

        return itemButton.getCustomItemStack(player);
    }
}
