package fr.maxlego08.shop.inventory.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.inventory.ItemButton;
import fr.maxlego08.shop.inventory.VInventory;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.save.inventory.ConfigBuyInventory;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.factories.ShopItem;
import fr.maxlego08.shop.zshop.inventories.InventoryObject;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryShopBuy extends VInventory {

	private AtomicInteger amount = new AtomicInteger(1);
	private ShopItem item;

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

		InventoryObject object = getInventoryObject();
		item = (ShopItem) args[0];
		final int lastPage = (int) args[1];

		createInventory(ConfigBuyInventory.inventoryName.replace("%item%", itemName(item.getDisplayItem())),
				ConfigBuyInventory.inventorySize);

		ConfigBuyInventory.decoration.forEach((slot, button) -> addItem(slot, button.getInitButton()));

		addItem(ConfigBuyInventory.resetItemintSlot,
				new ItemButton(Lang.resetItemButton.getInitButton()).setClick(event -> {
					amount.set(1);
					setItem();
				}));
		addItem(ConfigBuyInventory.removeTenItemintSlot,
				new ItemButton(Lang.removeTenItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(-10);
					setItem();
				}));
		addItem(ConfigBuyInventory.removeOneItemintSlot,
				new ItemButton(Lang.removeOneItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(-1);
					setItem();
				}));

		addItem(ConfigBuyInventory.addOneItemintSlot,
				new ItemButton(Lang.addOneItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(1);
					setItem();
				}));
		addItem(ConfigBuyInventory.addTenItemintSlot,
				new ItemButton(Lang.addTenItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(10);
					setItem();
				}));
		addItem(ConfigBuyInventory.maxItemintSlot,
				new ItemButton(Lang.maxItemButton.getInitButton("%max%", String.valueOf(item.getMaxStackSize())))
						.setClick(event -> {
							amount.set(item.getMaxStackSize());
							setItem();
						}));

		addItem(ConfigBuyInventory.backintSlotSlot,
				new ItemButton(Lang.backBuyButton.getInitButton()).setClick(event -> {
					main.getShop().openShop(player, EnumCategory.SHOP, lastPage, object.getId(),
							Permission.SHOP_OPEN.getPermission(item.getCategory()),
							main.getCategories().getCategory(item.getCategory()));
				}));
		addItem(ConfigBuyInventory.buyintSlotSlot, new ItemButton(Lang.buyButton.getInitButton()).setClick(event -> {
			item.performBuy(player, amount.get());
		}));

		setItem();

		return true;
	}

	public void setItem() {
		if (amount.get() > item.getMaxStackSize())
			amount.set(item.getMaxStackSize());
		if (amount.get() < 1)
			amount.set(1);

		ItemStack itemStack = item.getItem().clone();
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<String>();
		lore.addAll(
				ConfigBuyInventory.lore.stream()
						.map(string -> string.replace("%currency%", item.getEconomyType().toCurrency())
								.replace("%price%", format((item.getBuyPrice() * amount.get()))))
						.collect(Collectors.toList()));
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		itemStack.setAmount(amount.get());
		getInventory().setItem(ConfigBuyInventory.itemSlot, itemStack);
	}

	@Override
	protected void onClose(InventoryCloseEvent event, ZShop plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDrag(InventoryDragEvent event, ZShop plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public VInventory clone() {
		return new InventoryShopBuy();
	}

}
