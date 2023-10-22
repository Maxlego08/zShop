package fr.maxlego08.zshop.placeholder;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemButtonPlaceholder implements ReturnBiConsumer<Player, String, String> {

    private final ShopPlugin plugin;
    private final ShopManager shopManager;
    private final Pattern regex = Pattern.compile("([a-zA-Z_]+)(\\d*)");

    public ItemButtonPlaceholder(ShopPlugin plugin, ShopManager shopManager) {
        this.plugin = plugin;
        this.shopManager = shopManager;
    }

    @Override
    public String accept(Player player, String args) {

        for (ItemButtonPlaceholderKey key : ItemButtonPlaceholderKey.values()) {
            if (args.toLowerCase().startsWith(key.name().toLowerCase())) {
                args = args.replace(key.name().toLowerCase() + "_", "");
                Matcher matcher = regex.matcher(args);
                boolean find = matcher.find();
                if (find) {
                    String material = matcher.group(1);
                    String arg = matcher.group(2);
                    if (material.endsWith("_")) material = material.substring(0, material.length() - 1);
                    Optional<ItemButton> optional = this.shopManager.getItemButton(material);
                    if (optional.isPresent()) return key.consumer.accept(player, optional.get(), arg);
                    return "material " + material + " not found";
                }

                ItemButton button = shopManager.getCache(player).getItemButton();
                if (button != null){
                    return key.consumer.accept(player, button, args);
                }

                return "matcher for " + args + " not found";
            }
        }
        return "arg " + args + " not found";
    }

    enum ItemButtonPlaceholderKey {

        /* Sell price format with price modifier */
        SELL_PRICE_FORMAT((player, itemButton, arg) -> itemButton.getSellPriceFormat(player, arg.matches("\\d+") ? Integer.parseInt(arg) : 1)),

        /* Buy price format with price modifier */
        BUY_PRICE_FORMAT((player, itemButton, arg) -> itemButton.getBuyPriceFormat(player, arg.matches("\\d+") ? Integer.parseInt(arg) : 1)),

        /* Default sell price format */
        REAL_SELL_PRICE_FORMAT((player, itemButton, arg) -> itemButton.getSellPriceFormat(arg.matches("\\d+") ? Integer.parseInt(arg) : 1)),

        /* Default buy price format */
        REAL_BUY_PRICE_FORMAT((player, itemButton, arg) -> itemButton.getBuyPriceFormat(arg.matches("\\d+") ? Integer.parseInt(arg) : 1)),

        /* Max stack */
        MAX_STACK((player, itemButton, arg) -> String.valueOf(itemButton.getMaxStack())),

        ;

        private final ReturnTriConsumer<Player, ItemButton, String, String> consumer;

        ItemButtonPlaceholderKey(ReturnTriConsumer<Player, ItemButton, String, String> consumer) {
            this.consumer = consumer;
        }

        public ReturnTriConsumer<Player, ItemButton, String, String> getConsumer() {
            return consumer;
        }
    }

}
