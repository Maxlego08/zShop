package fr.maxlego08.shop.button.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.ShowButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.sound.SoundOption;
import fr.maxlego08.shop.button.ZButton;

public class ZShowButton extends ZButton implements ShowButton {

	private final List<String> lore;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param lore
	 */
	public ZShowButton(ButtonType type, ItemStack itemStack, int slot, List<String> lore, boolean isPermanent, SoundOption sound, boolean isClose) {
		super(type, itemStack, slot, isPermanent, sound, isClose);
		this.lore = lore;
	}

	@Override
	public List<String> getLore() {
		return lore;
	}

	@Override
	public List<String> getLore(Player player, ItemButton button, int amount, InventoryType type) {
		return lore.stream().map(line -> {

			line = line.replace("%sellPrice%",
					button.getSellPriceAsString(player, (type == InventoryType.SELL ? amount : 1)));
			line = line.replace("%buyPrice%",
					button.getBuyPriceAsString(player, (type == InventoryType.BUY ? amount : 1)));

			line = line.replace("%currency%", button.getEconomy().getCurrenry());
			line = line.replace("&", "§");
			return line;
		}).collect(Collectors.toList());
	}

	@Override
	public ItemStack applyLore(Player player, ItemButton button, int amount, InventoryType type) {
		ItemStack itemStack = button.getItemStack().clone();
		itemStack.setAmount(amount);
		List<String> lore = new ArrayList<>();
		ItemMeta itemMeta = itemStack.getItemMeta();

		if (itemMeta == null)
			return itemStack;

		if (itemMeta.hasLore())
			lore.addAll(itemMeta.getLore());
		lore.addAll(getLore(player, button, amount, type));
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return papi(itemStack, player);
	}

}
