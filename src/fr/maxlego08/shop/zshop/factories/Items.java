package fr.maxlego08.shop.zshop.factories;

import java.util.List;

import fr.maxlego08.shop.zcore.utils.storage.Saveable;
import fr.maxlego08.shop.zshop.items.ShopItem;

public interface Items extends Saveable{

	public List<ShopItem> getItems(int category);
	
}
