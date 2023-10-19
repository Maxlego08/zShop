package fr.maxlego08.zshop.save;

import fr.maxlego08.zshop.ZPriceModifier;
import fr.maxlego08.zshop.api.PriceModifier;
import fr.maxlego08.zshop.zcore.logger.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {

    public static int configVersion = 1;
    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;
    public static String defaultEconomy = "VAULT";
    public static String sellInventoryName = "shop_sell";
    public static String confirmInventoryName = "confirm";
    public static String buyInventoryName = "shop_buy";
    public static String dateFormat = "EEEE, d MMM yyyy HH:mm:ss";
    public static List<String> defaultLore = Arrays.asList(
            "&f» &7Buying price&8: &e%buyPrice%",
            "&f» &7Selling price&8: &e%sellPrice%",
            "",
            "&f➥ &r&7Left click to &f&nʙᴜʏ",
            "&f➥ &r&7Click wheel (or drop key) to &f&nsᴇʟʟ ᴇᴠᴇʀʏᴛʜɪɴɢ",
            "&f➥ &r&7Right click to &f&nsᴇʟʟ"
    );
    public static List<PriceModifier> priceModifiers = new ArrayList<>();

    private Config() {
    }

    public static void load(YamlConfiguration configuration) {

        int version = configuration.getInt("configVersion", -1);
        if (version != configVersion && version != -1) {
            Logger.info("You are not using the latest version of the configuration, items can be added on deleted. Please update your configuration.", Logger.LogType.WARNING);
        }

        enableDebug = configuration.getBoolean("enableDebug");
        enableDebugTime = configuration.getBoolean("enableDebugTime");

        defaultLore = configuration.getStringList("defaultLore");
        defaultEconomy = configuration.getString("defaultEconomy");
        confirmInventoryName = configuration.getString("confirmInventoryName");
        sellInventoryName = configuration.getString("sellInventoryName");
        buyInventoryName = configuration.getString("buyInventoryName");
        dateFormat = configuration.getString("dateFormat");
        priceModifiers = ((List<Map<String, Object>>) configuration.getList("pricesModifier", new ArrayList<>())).stream().map(ZPriceModifier::new).collect(Collectors.toList());

        LogConfig.load(configuration);

    }

}
