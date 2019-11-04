package fr.maxlego08.shop.save.inventory;

import java.util.HashMap;
import java.util.Map;

import fr.maxlego08.shop.zcore.utils.inventory.Button;

public class ConfigDefaultInventory {

	public static int inventorySize = 27;
	public static String inventoryName = "§eShop";
	public static Map<Integer, Button> decoration = new HashMap<Integer, Button>();

	static{
		for (int a = 0; a != 27; a++)
			decoration.put(a, new Button(null, 160, 9));
	}
	

}
