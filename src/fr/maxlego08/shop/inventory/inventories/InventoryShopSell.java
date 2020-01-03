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
import fr.maxlego08.shop.save.inventory.ConfigSellInventory;
import fr.maxlego08.shop.zcore.utils.enums.Permission;
import fr.maxlego08.shop.zshop.items.ShopItem;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryShopSell extends VInventory {

	private AtomicInteger amount = new AtomicInteger(1);
	private ShopItem item;

	@Override
	public boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception {

		item = (ShopItem) args[0];
		final int lastPage = (int) args[1];

		createInventory(ConfigSellInventory.inventoryName.replace("%item%", itemName(item.getDisplayItem())),
				ConfigSellInventory.inventorySize);

		ConfigSellInventory.decoration.forEach((slot, button) -> addItem(slot, button.getInitButton()));

		addItem(ConfigSellInventory.resetItemintSlot,
				new ItemButton(Lang.resetItemButton.getInitButton()).setClick(event -> {
					amount.set(1);
					setItem();
				}));
		addItem(ConfigSellInventory.removeTenItemintSlot,
				new ItemButton(Lang.removeTenItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(-1);
					setItem();
				}));
		addItem(ConfigSellInventory.removeOneItemintSlot,
				new ItemButton(Lang.removeOneItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(-10);
					setItem();
				}));

		addItem(ConfigSellInventory.addOneItemintSlot,
				new ItemButton(Lang.addOneItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(1);
					setItem();
				}));
		addItem(ConfigSellInventory.addTenItemintSlot,
				new ItemButton(Lang.addTenItemButton.getInitButton()).setClick(event -> {
					amount.getAndAdd(10);
					setItem();
				}));
		addItem(ConfigSellInventory.maxItemintSlot,
				new ItemButton(Lang.maxItemButton.getInitButton("%max%", String.valueOf(item.getMaxStackSize())))
						.setClick(event -> {
							amount.set(item.getMaxStackSize());
							setItem();
						}));

		addItem(ConfigSellInventory.backintSlotSlot,
				new ItemButton(Lang.backBuyButton.getInitButton()).setClick(event -> {
					main.getShop().openShop(player, EnumCategory.SHOP, lastPage,
							Permission.SHOP_OPEN_CONFIRM.getPermission(item.getCategory()),
							main.getCategories().getCategory(item.getCategory()));
				}));
		addItem(ConfigSellInventory.sellintSlotSlot, new ItemButton(Lang.sellButton.getInitButton()).setClick(event -> {
			item.performSell(player, amount.get());
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
		lore.addAll(ConfigSellInventory.lore.stream()
				.map(string -> string.replace("%price%", format((item.getSellPrice() * amount.get()))))
				.collect(Collectors.toList()));
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		itemStack.setAmount(amount.get());
		getInventory().setItem(ConfigSellInventory.itemSlot, itemStack);
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
		return new InventoryShopSell();
	}

}
