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

		String inventoryName = category.getType().equals(ShopType.ITEM)
				? Lang.shopInventoryItem.replace("%page%", String.valueOf(page)).replace("%maxPage%",
						String.valueOf(getMaxPage(category.getInventorySize() - 9, items)))
				: Lang.shopInventoryUniqueItem;

		createInventory(inventoryName.replace("%category%", category.getName()), category.getInventorySize());

		if (category.getType().equals(ShopType.ITEM)) {

			Pagination<ShopItem> pagination = new Pagination<>();
			AtomicInteger slot = new AtomicInteger();

			int pageSize = items.size() < category.getInventorySize() ? items.size() : category.getInventorySize() - 9;

			pagination.paginate(items, pageSize, page).forEach(item -> {
				addItem(slot.getAndIncrement(), new ItemButton(item.getDisplayItem()).setLeftClick(event -> {
					if (item.getBuyPrice() > 0)
						main.getShop().openShop(player, EnumCategory.BUY, 1, item, page);
				}).setRightClick(event -> {
					;
				}).setMiddleClick(event -> {
					if (item.getSellPrice() > 0)
						item.performSell(player, 0);
				}));
			});

			if (getPage() != 1)
				addItem(category.getPreviousButtonSlot(),
						new ItemButton(Lang.previousButton.getInitButton()).setClick(event -> {
							main.getShop().openShop(player, EnumCategory.SHOP, page - 1, args);
						}));
			if (getPage() != getMaxPage(category.getInventorySize() - 9, items))
				addItem(category.getNexButtonSlot(), new ItemButton(Lang.nextButton.getInitButton()).setClick(event -> {
					main.getShop().openShop(player, EnumCategory.SHOP, page + 1, args);
				}));
		} else {

		}

		addItem(category.getBackButtonSlot(), new ItemButton(Lang.backButton.getInitButton())
				.setClick(event -> main.getShop().openShop(player, EnumCategory.DEFAULT, 0)));

		return true;
	}

	private int getMaxPage(int size, List<ShopItem> items) {
		if (size == 0)
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
