package fr.maxlego08.shop.event.events;

import org.bukkit.entity.Player;

import fr.maxlego08.shop.event.ShopEvent;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class ShopOpenEvent extends ShopEvent {

	private final Player player;
	private EnumCategory category;

	public ShopOpenEvent(Player player, EnumCategory category) {
		super();
		this.player = player;
		this.category = category;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the category
	 */
	public EnumCategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(EnumCategory category) {
		this.category = category;
	}

}
