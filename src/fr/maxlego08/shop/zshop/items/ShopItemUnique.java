package fr.maxlego08.shop.zshop.items;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.event.events.ShopPreBuyEvent;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.ZUtils;

public class ShopItemUnique extends ZUtils implements ShopItem {

	private final int id;
	private final int slot;
	private final ItemStack displayItem;
	private final double buyPrice;
	private final boolean useConfirm;
	private final List<String> commands;
	private final ItemStack giveItem;

	public ShopItemUnique(int id, int slot, ItemStack displayItem, double buyPrice, boolean useConfirm,
			List<String> commands, ItemStack giveItem) {
		super();
		this.id = id;
		this.slot = slot;
		this.displayItem = displayItem;
		this.buyPrice = buyPrice;
		this.useConfirm = useConfirm;
		this.commands = commands;
		this.giveItem = giveItem;
	}

	@Override
	public ShopType getType() {
		return ShopType.UNIQUE_ITEM;
	}

	public List<String> getCommands() {
		return commands;
	}

	public boolean useConfirm() {
		return useConfirm;
	}

	@Override
	public int getCategory() {
		return id;
	}

	@Override
	public int getSlot() {
		return slot;
	}

	@Override
	public void performBuy(Player player, int amount) {
		double tmpPrice = buyPrice;
		if (getBalance(player) < tmpPrice) {
			player.sendMessage(Lang.prefix + " " + Lang.notEnouhtMoney);
			return;
		}

		if (hasInventoryFull(player)) {
			player.sendMessage(Lang.prefix + " " + Lang.notEnouhtPlace);
			return;
		}

		if (Config.shopPreBuyEvent) {
			ShopPreBuyEvent event = new ShopPreBuyEvent(this, player, amount, tmpPrice);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;

			tmpPrice = event.getPrice();
			amount = event.getQuantity();
		}

		withdrawMoney(player, buyPrice);
		String itemName = getItemName(getDisplayItem());

		if (giveItem != null)
			give(player, giveItem);

		if (commands != null)
			for (String command : commands)
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						command.replace("%amount%", String.valueOf(amount)).replace("%item%", itemName)
								.replace("%price%", format(tmpPrice)).replace("%player%", player.getName()));

		player.sendMessage(Lang.prefix + " " + Lang.buyUniqueItem.replace("%amount%", String.valueOf(amount))
				.replace("%item%", itemName).replace("%price%", format(tmpPrice)));

		if (Config.logConsole)
			Logger.info(player.getName() + " vient d'acheter x" + amount + " " + itemName + " pour " + format(tmpPrice)
					+ "$", LogType.INFO);
	}

	@Override
	public void performSell(Player player, int amount) {

	}

	@Override
	public ItemStack getItem() {
		return giveItem;
	}

	@Override
	public ItemStack getDisplayItem() {
		ItemStack tmpitem = displayItem.clone();
		ItemMeta itemMeta = tmpitem.getItemMeta();
		itemMeta.setDisplayName(itemMeta.getDisplayName().replace("%price%", format(buyPrice)));
		itemMeta.setLore(itemMeta.getLore().stream().map(string -> string.replace("%price%", format(buyPrice)))
				.collect(Collectors.toList()));
		tmpitem.setItemMeta(itemMeta);
		return tmpitem;
	}

	@Override
	public double getSellPrice() {
		return 0;
	}

	@Override
	public double getBuyPrice() {
		return buyPrice;
	}

	@Override
	public int getMaxStackSize() {
		return 0;
	}

}
