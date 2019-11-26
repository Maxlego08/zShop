package fr.maxlego08.shop.inventory.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.inventory.ItemButton;
import fr.maxlego08.shop.inventory.VInventory;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.save.inventory.ConfigInventoryConfirm;
import fr.maxlego08.shop.zshop.items.ShopItem;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryShopConfirm extends VInventory {

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

		ShopItem item = (ShopItem) args[0];

		createInventory(ConfigInventoryConfirm.inventoryName, ConfigInventoryConfirm.inventorySize);

		ConfigInventoryConfirm.decoration.forEach((slot, button) -> addItem(slot, button.getInitButton()));

		addItem(ConfigInventoryConfirm.displayItem, item.getDisplayItem());

		addItem(ConfigInventoryConfirm.backButton,
				new ItemButton(Lang.backBuyButton.getInitButton()).setClick(event -> {
					main.getShop().openShop(player, EnumCategory.SHOP, 1,
							main.getCategories().getCategory(item.getCategory()));
				}));

		addItem(ConfigInventoryConfirm.confirmButtonSlot,
				new ItemButton(Lang.confirmButton.getInitButton()).setClick(event -> {
					item.performBuy(player, 0);
					main.getShop().openShop(player, EnumCategory.SHOP, 1,
							main.getCategories().getCategory(item.getCategory()));
				}));

		return true;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {

	}

	@Override
	public VInventory clone() {
		return new InventoryShopConfirm();
	}

}
