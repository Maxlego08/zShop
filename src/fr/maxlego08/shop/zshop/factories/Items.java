package fr.maxlego08.shop.zshop.factories;

import java.util.List;

import fr.maxlego08.shop.zshop.items.ShopItem;

public interface Items {

	/**
	 * Allows you to retrieve all items in a category
	 * @param category id
	 * @return items of category
	 */
	List<ShopItem> getItems(int category);

	/**
	 * Allows you to change the items of a category
	 * @param items list
	 * @param id of category
	 */
	void setItems(List<ShopItem> items, int id);
	
	/**
	 * Load items
	 * 
	 * Here's an example to add an item with all the options you can add to the item:
	 * 
	 * '1': -> Corresponds to the item id in its category
     *   type: ITEM -> the type of the item, either ITEM or UNIQUE_ITEM
     *   item: -> Itemstack creation
     *     id: 3 -> material id
     *     data: 0 -> the data of the material
     *     stack: 64 -> Limit the number of items sold or purchased (Useful for chicken eggs, cake, enderpearl etc ...)
     *     name: "Random &cColored &fName" -> Item name, with the color code "&"
     *     lore: -> Lore of the item, with the color code "&"
     *       - "&fLine &51"
     *       - "&fLine &22"
     *   buyPrice: 30.0 -> Purchase price, if the price is 0 then the item can not be bought
     *   sellPrice: 2.0 -> Selling price, if the price is 0 then the item can not be sold
	 * 
	 */
	void load();
	
	/**
	 * Save items
	 */
	void save();

}
