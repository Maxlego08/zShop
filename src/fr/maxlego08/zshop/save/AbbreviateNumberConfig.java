package fr.maxlego08.zshop.save;

import org.bukkit.configuration.file.YamlConfiguration;

public class AbbreviateNumberConfig {

    public static boolean enable = false;
    public static String thousand = "k"; // 1.000 = 1k
    public static String millions = "m"; // 1.000.000 = 1m
    public static String billion = "b"; // 1.000.000.000 = 1b
    public static String trillion = "t"; // 1.000.000.000.000 = 1t
    public static String quadrillion = "q"; // 1.000.000.000.000.000 = 1q
    public static String quintillion = "Q"; // 1.000.000.000.000.000.000 = 1Q

    public static void load(YamlConfiguration configuration) {
        enable = configuration.getBoolean("priceFormat.abbreviatedNumber.enable", false);
        thousand = configuration.getString("priceFormat.abbreviatedNumber.thousand", "k");
        millions = configuration.getString("priceFormat.abbreviatedNumber.millions", "m");
        billion = configuration.getString("priceFormat.abbreviatedNumber.billion", "b");
        trillion = configuration.getString("priceFormat.abbreviatedNumber.trillion", "t");
        quadrillion = configuration.getString("priceFormat.abbreviatedNumber.quadrillion", "q");
        quintillion = configuration.getString("priceFormat.abbreviatedNumber.quintillion", "Q");
    }

}
