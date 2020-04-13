package fr.maxlego08.shop.inventory.inventories;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.inventory.ItemButton;
import fr.maxlego08.shop.inventory.VInventory;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.categories.Category;
import fr.maxlego08.shop.zshop.inventories.InventoryObject;
import fr.maxlego08.shop.zshop.items.ShopItem;
import fr.maxlego08.shop.zshop.items.ShopItemConsomable;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryShopConfig extends VInventory {

	private InventoryObject object;
	
	public InventoryShopConfig() {

		this.disableClick = false;

	}

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

		object = getInventoryObject();

		Category category = (Category) args[0];

		List<ShopItem> items = main.getItems().getItems(category.getId());

		int pageSize = items.size() < (category.getInventorySize() - 9) ? items.size()
				: (category.getInventorySize() - 9);

		int maxPage = main.getItems().getMaxPage(items, category, pageSize);

		/**
		 * Création du nom de l'inventaire en fonction de son type
		 */
		String inventoryName = category.isItem() ? Lang.shopInventoryItem : Lang.shopInventoryUniqueItem;

		inventoryName = inventoryName.replace("%page%", String.valueOf(page)).replace("%maxPage%",
				String.valueOf(maxPage));

		createInventory(inventoryName.replace("%category%", category.getName()), category.getInventorySize());

		if (!category.getType().equals(ShopType.ITEM)) {
			object.getDecorations(category.getId()).forEach((slot, button) -> addItem(slot, button.getInitButton()));
		}

		if (category.getType().equals(ShopType.ITEM_SLOT)) {

			List<ShopItemConsomable> itemConsomables = main.getItems().shorItems(items, category.getInventorySize(),
					maxPage, page);

			itemConsomables.forEach(item -> addItem(item.getTmpSlot(), item.getDisplayItem()));

		} else {

			items.forEach(item -> addItem(item.getSlot(), new ItemButton(item.getDisplayItem())));

		}

		if (category.getType().equals(ShopType.ITEM_SLOT)) {
			/**
			 * Ajout des boutons pour changer de page si besoin
			 */
			if (getPage() != 1)
				addItem(category.getPreviousButtonSlot(),
						new ItemButton(Lang.previousButton.getInitButton()).setClick(event -> {
							main.getShop().openShop(player, EnumCategory.SHOP, page - 1, object.getId(),
									Permission.SHOP_OPEN.getPermission(category.getId()), args);
						}));
			if (getPage() != maxPage)
				addItem(category.getNexButtonSlot(), new ItemButton(Lang.nextButton.getInitButton()));
		}

		addItem(category.getBackButtonSlot(), new ItemButton(Lang.backButton.getInitButton()));

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
		return new InventoryShopConfig();
	}

}
