package fr.maxlego08.shop.api.button;

public interface ItemButton extends IButton{

	/**
	 * 
	 * @return sell price
	 */
	public double getSellPrice();
	
	/**
	 * 
	 * @return buy price
	 */
	public double getBuyPrice();
	
	/**
	 * 
	 * @return can sell
	 */
	public boolean canSell();
	
	/**
	 * 
	 * @return can buy
	 */
	public boolean canBuy();
	
}
