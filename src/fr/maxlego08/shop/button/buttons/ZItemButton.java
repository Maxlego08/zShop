package fr.maxlego08.shop.button.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.enums.ButtonType;
import fr.maxlego08.shop.api.enums.Economy;

public class ZItemButton extends ZPermissibleButton implements ItemButton {

	private final double sellPrice;
	private final double buyPrice;
	private final Economy economy;
	
	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param sellPrice
	 * @param buyPrice
	 * @param economy
	 */
	public ZItemButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, double sellPrice, double buyPrice, Economy economy) {
		super(type, itemStack, slot, permission, message, elseButton);
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.economy = economy;
	}

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param sellPrice
	 * @param buyPrice
	 * @param economy
	 */
	public ZItemButton(ButtonType type, ItemStack itemStack, int slot, double sellPrice, double buyPrice,
			Economy economy) {
		super(type, itemStack, slot);
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.economy = economy;
	}

	@Override
	public double getSellPrice() {
		return sellPrice;
	}

	@Override
	public double getBuyPrice() {
		return buyPrice;
	}

	@Override
	public boolean canSell() {
		return sellPrice != 0.0;
	}

	@Override
	public boolean canBuy() {
		return buyPrice != 0;
	}

	@Override
	public Economy getEconomy() {
		return economy == null ? Economy.VAULT : economy;
	}

	@Override
	public boolean needToConfirm() {
		return super.getType() == ButtonType.ITEM_CONFIRM;
	}

	@Override
	public void buy(Player player, int amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sell(Player player, int amount) {
		// TODO Auto-generated method stub

	}

}
