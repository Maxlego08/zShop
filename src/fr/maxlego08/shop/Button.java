package fr.maxlego08.shop;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.IButton;
import fr.maxlego08.shop.api.button.ItemButton;
import fr.maxlego08.shop.api.enums.ButtonType;

public class Button implements ItemButton {

	@Override
	public ItemStack getItemStack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ButtonType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IButton getElseButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPermission() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSlot() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSellPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getBuyPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canSell() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBuy() {
		// TODO Auto-generated method stub
		return false;
	}

}
