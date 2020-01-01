package fr.maxlego08.shop.zshop.factories;

import java.util.List;

import org.bukkit.Material;

import fr.maxlego08.shop.zshop.items.ShopItem;

public interface Items {

	/**
	 * Allows you to retrieve all items in a category
	 * @param category id
	 * @return items of category
	 */
	List<ShopItem> getItems(int category);
	
	/**
	 * Allows you to retrieve an item list based on a material
	 * @param material
	 * @return items that have the same material as in parameter 
	 */
	List<ShopItem> getItems(Material material);

	/**
	 * Allows to check if at least one item is in the shop with the given material
	 * @param material
	 * @return true if items was found
	 */
	boolean hasItems(Material material);
	
	/**
	 * Allows you to change the items of a category
	 * @param items list
	 * @param id of category
	 */
	void setItems(List<ShopItem> items, int id);
	
	/**
	 * Load items
	 * 
	 * <p>Here's an example to add an <b>item</b> with all the options you can add to the item:
	 * <br>
	 * <br>'1': -> Corresponds to the item id in its category
     * <br>  type: ITEM -> the type of the item, either ITEM or UNIQUE_ITEM
     * <br>  item: -> Itemstack creation
     * <br>
     * <br>    id: 3 -> material id
     * <br>    data: 0 -> the data of the material
     * <br>    stack: 64 -> Limit the number of items sold or purchased (Useful for chicken eggs, cake, enderpearl etc ...)
     * <br>    name: "Random &cColored &fName" -> Item name, with the color code "&"
     * <br>
     * <br>    lore: -> Lore of the item, with the color code "&"
     * <br>      - "&fLine &51"
     * <br>      - "&fLine &22"
     * <br>
     * <br>    enchants: -> Add enchantments
     * <br>      - DAMAGE_ALL,5
     * <br>
     * <br>    flags: -> Add ItemFlag
     * <br>      - HIDE_ENCHANTS 
     * <br>
     * <br>  buyPrice: 30.0 -> Purchase price, if the price is 0 then the item can not be bought
     * <br>  sellPrice: 2.0 -> Selling price, if the price is 0 then the item can not be sold
     * <br>
     * <br>  giveItem: true -> Give the item or not when buying
     * <br>  executeSellCommand: false -> Execute orders when an item is sold
     * <br>  executeBuyCommand: true -> execute orders when an item is purchased
     * <br>  commands: -> Commands run when buying or selling
     * <br>    - "bc %player% was just bought x%amount% %item% in shop !"
     * <br>
     * </p>
	 * 
	 */
	void load();
	
	/**
	 * Save items
	 */
	void save(String fileName);

}
