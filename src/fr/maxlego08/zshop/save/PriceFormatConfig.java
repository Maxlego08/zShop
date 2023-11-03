package fr.maxlego08.zshop.save;

import org.bukkit.configuration.file.YamlConfiguration;

public class PriceFormatConfig {

    public static String format;
    public static char decimalSeparator;
    public static char groupingSeparator;

    public static int minimumIntegerDigits;
    public static int maximumIntegerDigits;
    public static int minimumFractionDigits;
    public static int maximumFractionDigits;

    public static void load(YamlConfiguration configuration) {
        format = configuration.getString("priceFormat.format");
        decimalSeparator = configuration.getString("priceFormat.decimalSeparator", ",").charAt(0);
        groupingSeparator = configuration.getString("priceFormat.groupingSeparator", ".").charAt(0);
        minimumIntegerDigits = configuration.getInt("priceFormat.minimumIntegerDigits", 1);
        maximumIntegerDigits = configuration.getInt("priceFormat.maximumIntegerDigits", 8);
        minimumFractionDigits = configuration.getInt("priceFormat.minimumFractionDigits", 0);
        maximumFractionDigits = configuration.getInt("priceFormat.maximumFractionDigits", 2);
    }
}
