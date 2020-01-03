package fr.maxlego08.shop.inventory.inventories;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.inventory.ItemButton;
import fr.maxlego08.shop.inventory.VInventory;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zcore.utils.inventory.Pagination;
import fr.maxlego08.shop.zshop.categories.Category;
import fr.maxlego08.shop.zshop.items.ShopItem;
import fr.maxlego08.shop.zshop.items.ShopItem.ShopType;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryShopCategory extends VInventory {

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

		Category category = (Category) args[0];
		List<ShopItem> items = main.getItems().getItems(category.getId());

		int pageSize = items.size() < (category.getInventorySize() - 9) ? items.size()
				: (category.getInventorySize() - 9);

		/**
		 * Création du nom de l'inventaire en fonction de son type
		 */
		String inventoryName = category.getType().equals(ShopType.ITEM)
				? Lang.shopInventoryItem.replace("%page%", String.valueOf(page)).replace("%maxPage%",
						String.valueOf(getMaxPage(pageSize, items)))
				: Lang.shopInventoryUniqueItem;
		createInventory(inventoryName.replace("%category%", category.getName()), category.getInventorySize());

		/**
		 * Notre items est un item de type ITEM, donc on entre dans la condition
		 */
		if (category.getType().equals(ShopType.ITEM)) {

			Pagination<ShopItem> pagination = new Pagination<>();
			AtomicInteger slot = new AtomicInteger();

			/*
			 * Système de pagination pour ajouter les items
			 */
			pagination.paginate(items, pageSize, page).forEach(item -> {
				addItem(slot.getAndIncrement(), new ItemButton(item.getDisplayItem()).setLeftClick(event -> {
					if (item.getBuyPrice() > 0)
						main.getShop().openShop(player, EnumCategory.BUY, 1, Permission.SHOP_OPEN_BUY, item, page);
				}).setRightClick(event -> {
					if (item.getSellPrice() > 0)
						main.getShop().openShop(player, EnumCategory.SELL, 1, Permission.SHOP_OPEN_SELL, item, page);
				}).setMiddleClick(event -> {
					if (item.getSellPrice() > 0)
						item.performSell(player, 0);
				}));
			});

			/**
			 * Ajout des boutons pour changer de page si besoin
			 */
			if (getPage() != 1)
				addItem(category.getPreviousButtonSlot(),
						new ItemButton(Lang.previousButton.getInitButton()).setClick(event -> {
							main.getShop().openShop(player, EnumCategory.SHOP, page - 1,
									Permission.SHOP_OPEN.getPermission(category.getId()), args);
						}));
			if (getPage() != getMaxPage(pageSize, items))
				addItem(category.getNexButtonSlot(), new ItemButton(Lang.nextButton.getInitButton()).setClick(event -> {
					main.getShop().openShop(player, EnumCategory.SHOP, page + 1,
							Permission.SHOP_OPEN.getPermission(category.getId()), args);
				}));
		} else {

			items.forEach(item -> {

				addItem(item.getSlot(), new ItemButton(item.getDisplayItem()).setClick(event -> {
					if (item.useConfirm())
						main.getShop().openShop(player, EnumCategory.CONFIRM, 1, Permission.SHOP_OPEN_CONFIRM, item);
					else
						item.performBuy(player, 1);
				}));

			});

		}

		addItem(category.getBackButtonSlot(), new ItemButton(Lang.backButton.getInitButton())
				.setClick(event -> main.getShop().openShop(player, EnumCategory.DEFAULT, 0, Permission.SHOP_USE)));

		return true;
	}

	private int getMaxPage(int size, List<ShopItem> items) {
		if (size == 0)
			return 1;
		if (size == items.size())
			return 1;
		return (items.size() / size) + 1;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {

	}

	@Override
	public VInventory clone() {
		return new InventoryShopCategory();
	}

}
