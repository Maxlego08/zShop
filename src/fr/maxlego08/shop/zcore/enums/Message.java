package fr.maxlego08.shop.zcore.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.zcore.utils.nms.NMSUtils;

public enum Message {

	PREFIX("§8(§bzShop§8) "),
	
	TELEPORT_MOVE("§cYou must not move!"),
	TELEPORT_MESSAGE("§7Teleportation in §3%second% §7seconds!"),
	TELEPORT_ERROR("§cYou already have a teleportation in progress!"),
	TELEPORT_SUCCESS("§7Teleportation done!"),
	
	INVENTORY_NULL("§cImpossible to find the inventory with the id §6%id%§c."),
	INVENTORY_CLONE_NULL("§cThe inventory clone is null!"),
	INVENTORY_OPEN_ERROR("§cAn error occurred with the opening of the inventory §6%id%§c."),
	INVENTORY_BUTTON_PREVIOUS("§f» §7Previous page"),
	INVENTORY_BUTTON_NEXT("§f» §7Next page"),
	
	TIME_DAY("%02d jour(s) %02d heure(s) %02d minute(s) %02d seconde(s)"),
	TIME_HOUR("%02d heure(s) %02d minute(s) %02d seconde(s)"),
	TIME_HOUR_SIMPLE("%02d:%02d:%02d"),
	TIME_MINUTE("%02d minute(s) %02d seconde(s)"),
	TIME_SECOND("%02d seconde(s)"),
	
	COMMAND_SYNTAXE_ERROR("§cYou must execute the command like this§7: §a%syntax%"),
	COMMAND_NO_PERMISSION("§cYou do not have permission to run this command."),
	COMMAND_NO_CONSOLE("§cOnly one player can execute this command."),
	COMMAND_NO_ARG("§cImpossible to find the command with its arguments."),
	COMMAND_SYNTAXE_HELP("§f%syntax% §7» §7%description%"),
	
	CANT_SELL("§cUnable to sell"),
	CANT_BUY("§cUnable to buy"),
	SELL_ITEM("§aYou just sold §6x%amount% %item%§a for §6%price%§a%currency% !"),
	BUY_ITEM("§aYou have just bought §6x%amount% %item%§a for §6%price%§a%currency% !"),
	NOT_ENOUGH_ITEMS("§cYou do not have enough items in your inventory."),

	BUY_LOG("%player% has purchased x%amount% %item% for %price%%currency%."),
	SELL_LOG("%player% has just sold x%amount% %item% for %price%%currency%."),

	NOT_ENOUGH_MONEY_VAULT("§cYou do not have enough money."),
	NOT_ENOUGH_MONEY_PLAYERPOINT("§cYou do not have enough points."),
	NOT_ENOUGH_MONEY_TOKENMANAGER("§cYou do not have enough token."),
	NOT_ENOUGH_MONEY_MYSQLTOKEN("§cYou do not have enough token."),
	NOT_ENOUGH_MONEY_LEVEL("§cYou do not have enough level."),
	NOT_ENOUGH_MONEY_OPTECO("§cYou do not have enough OE."),

	NOT_ENOUGH_PLACE("§cYour inventory is ful"),
	NOT_ITEMS("§cYou do not have this item in your inventory."),
	AND("and"),
	SELLALL_ERROR("§cYou don't have any item to sell."),
	SELLHAND_ALL("§aYou just sold §6%item% §afor §6%price%§a%currency%."),
	SELLHAND_ALLITEM("§ax§e%amount% §2%item%§a"),
	SELLHAND_EMPTY("§cUnable to find an item for sale."),
	SELLHAND_AIR("§cYou can't sell air !"),
	CATEGORY_EMPTY("§cThe name of the CATEGORY_ cannot be null."),
	CATEGORY_DOESNT_EXIST("§cThe §f%name% §cCATEGORY_ does not exist."),

	BUY_PERMISSION("§d%percent%% §bof reduction"),
	SELL_PERMISSION("§d%percent%% §bincrease"),
	BUY_PERMISSIONS_LINE("§bPrice reduced by §d%percent%§b%"),
	SELL_PERMISSION_LINE("§bPrice increased by §d%percent%§b%"),

	SECOND("second"),
	MINUTE("minute"),
	HOUR("hour"),
	DAY("day"),

	CURRENCY_VAULT("$"),
	CURRENCY_PLAYERPOINT("£"),
	CURRENCY_TOKENMANAGER("T"),
	CURRENCY_MYSQLTOKEN("MT"),
	CURRENCY_LEVEL("xp"),
	CURRENCY_OPTECO("oe"),

	PRICE_LONG_FORMAT(" "),
	PRICE_DOUBLE_FORMAT("#.##"),
	
	;

	private List<String> messages;
	private String message;
	private Map<String, Object> titles = new HashMap<>();
	private boolean use = true;
	private MessageType type = MessageType.TCHAT;

	private ItemStack itemStack;
	
	/**
	 * 
	 * @param message
	 */
	private Message(String message) {
		this.message = message;
		this.use = true;
	}

	/**
	 * 
	 * @param title
	 * @param subTitle
	 * @param a
	 * @param b
	 * @param c
	 */
	private Message(String title, String subTitle, int a, int b, int c) {
		this.use = true;
		this.titles.put("title", title);
		this.titles.put("subtitle", subTitle);
		this.titles.put("start", a);
		this.titles.put("time", b);
		this.titles.put("end", c);
		this.titles.put("isUse", true);
		this.type = MessageType.TITLE;
	}

	/**
	 * 
	 * @param message
	 */
	private Message(String... message) {
		this.messages = Arrays.asList(message);
		this.use = true;
	}
	
	/**
	 * 
	 * @param message
	 */
	private Message(MessageType type, String... message) {
		this.messages = Arrays.asList(message);
		this.use = true;
		this.type = type;
	}
	
	/**
	 * 
	 * @param message
	 */
	private Message(MessageType type, String message) {
		this.message = message;
		this.use = true;
		this.type = type;
	}

	/**
	 * 
	 * @param message
	 * @param use
	 */
	private Message(String message, boolean use) {
		this.message = message;
		this.use = use;
	}

	public String getMessage() {
		return message;
	}

	public String toMsg() {
		return message;
	}

	public String msg() {
		return message;
	}

	public boolean isUse() {
		return use;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getMessages() {
		return messages == null ? Arrays.asList(message) : messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public boolean isMessage() {
		return messages != null && messages.size() > 1;
	}

	public String getTitle() {
		return (String) titles.get("title");
	}

	public Map<String, Object> getTitles() {
		return titles;
	}

	public void setTitles(Map<String, Object> titles) {
		this.titles = titles;
		this.type = MessageType.TITLE;
	}

	public String getSubTitle() {
		return (String) titles.get("subtitle");
	}

	public boolean isTitle() {
		return titles.containsKey("title");
	}

	public int getStart() {
		return ((Number) titles.get("start")).intValue();
	}

	public int getEnd() {
		return ((Number) titles.get("end")).intValue();
	}

	public int getTime() {
		return ((Number) titles.get("time")).intValue();
	}

	public boolean isUseTitle() {
		return (boolean) titles.getOrDefault("isUse", "true");
	}

	public String replace(String a, String b) {
		return message.replace(a, b);
	}

	public MessageType getType() {
		return type.equals(MessageType.ACTION) && NMSUtils.isVeryOldVersion() ? MessageType.TCHAT : type;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
	
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

}

