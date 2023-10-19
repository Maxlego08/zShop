package fr.maxlego08.zshop.convert;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.zcore.logger.Logger;
import fr.maxlego08.zshop.zcore.utils.plugins.Plugins;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShopGuiPlusConverter {

    private boolean isConvert = false;
    private File zShopFolderConvert;
    private YamlConfiguration shopGuiPlusConfiguration;

    public void convert(ShopPlugin plugin, CommandSender sender) {

        this.zShopFolderConvert = new File(plugin.getDataFolder(), "inventories/convert");
        if (!this.zShopFolderConvert.exists()) this.zShopFolderConvert.mkdir();

        if (this.isConvert) {
            sender.sendMessage("§cConversion in progress, please wait.");
            return;
        }

        this.isConvert = true;

        File folder = new File("plugins/" + Plugins.SHOPGUIPLUS.getName());
        if (!folder.exists()) {
            sender.sendMessage("§f" + folder.getPath() + " §cfolder doesnt exist.");
            return;
        }

        loadMainConfig(folder, sender);
        loadShops(folder, sender);

        this.isConvert = false;
    }

    private void loadShops(File folder, CommandSender sender) {

        File folderShop = new File(folder, "shops/");
        if (!folderShop.exists()) {
            sender.sendMessage("§f" + folderShop.getPath() + " §cfolder doesnt exist.");
            return;
        }

        try (Stream<Path> stream = Files.walk(Paths.get(folderShop.getPath()))) {
            stream.skip(1).map(Path::toFile).filter(File::isFile).filter(e -> e.getName().endsWith(".yml")).forEach(file -> loadShop(file, sender));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void loadShop(File file, CommandSender sender) {
        sender.sendMessage("§fStart convert file §b" + file.getPath());

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        List<String> strings = new ArrayList<>(configuration.getConfigurationSection("").getKeys(false));
        if (strings.isEmpty()) {
            sender.sendMessage("§cImpossible to convert file §b" + file.getPath());
            return;
        }

        String path = strings.get(0) + ".";

        File newFile = new File(zShopFolderConvert, file.getName());

        if (newFile.exists()) {
            sender.sendMessage("§f" + newFile.getPath() + " already exist ! Stop convert " + file.getPath());
            return;
        }

        try {
            newFile.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        saveConfig(sender, newFile, YamlConfiguration.loadConfiguration(newFile), configuration, path);

        sender.sendMessage("§fEnd convert file §b" + file.getPath() + " §fto §a" + newFile.getPath());
    }

    private void saveConfig(CommandSender sender, File file, YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path) {

        configuration.set("name", shopGuiPlusConfiguration.get(path + "name", "Default name"));
        configuration.set("size", shopGuiPlusConfiguration.get(path + "size", 54));

        if (shopGuiPlusConfiguration.contains(path + "fillItem.")) {
            saveItemStack(configuration, shopGuiPlusConfiguration, path + "fillItem.", "", "fillItem.");
        }

        int size = saveItems(configuration, shopGuiPlusConfiguration, path + "items.", "item");
        sender.sendMessage("§fConvert §e" + size + " §fitems");

        try {
            saveButtons(configuration, shopGuiPlusConfiguration, path + "buttons.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void saveDefaultBack(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path) {
        String zShopPath = "items.goBack.";
        configuration.set(zShopPath + "type", "INVENTORY");
        configuration.set(zShopPath + "inventory", "shop");
        configuration.set(zShopPath + "plugin", "zShop");
        configuration.set(zShopPath + "slot", this.shopGuiPlusConfiguration.get("buttons.goBack.slot", 0));
        saveItemStack(configuration, this.shopGuiPlusConfiguration, "buttons.goBack.item.", "goBack");
    }

    private void saveDefaultPrevious(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path) {
        String zShopPath = "items.previousPage.";
        configuration.set(zShopPath + "type", "PREVIOUS");
        configuration.set(zShopPath + "slot", this.shopGuiPlusConfiguration.get("buttons.previousPage.slot", 0));
        saveItemStack(configuration, this.shopGuiPlusConfiguration, "buttons.previousPage.item.", "previousPage");
    }

    private void saveDefaultNext(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path) {
        String zShopPath = "items.nextPage.";
        configuration.set(zShopPath + "type", "NEXT");
        configuration.set(zShopPath + "slot", this.shopGuiPlusConfiguration.get("buttons.nextPage.slot", 0));
        saveItemStack(configuration, this.shopGuiPlusConfiguration, "buttons.nextPage.item.", "nextPage");
    }

    private void saveButtons(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path) {

        ConfigurationSection configurationSection = shopGuiPlusConfiguration.getConfigurationSection(path);

        saveDefaultBack(configuration, shopGuiPlusConfiguration, path);
        saveDefaultNext(configuration, shopGuiPlusConfiguration, path);
        saveDefaultPrevious(configuration, shopGuiPlusConfiguration, path);

        if (configurationSection == null) return;

        for (String key : configurationSection.getKeys(false)) {
            String newPath = path + key + ".";

            String zShopPath = "items." + key + ".";
            saveItemStack(configuration, shopGuiPlusConfiguration, newPath + "item.", key);

            switch (key) {

                case "goBack":
                    configuration.set(zShopPath + "type", "INVENTORY");
                    configuration.set(zShopPath + "inventory", "shop");
                    configuration.set(zShopPath + "plugin", "zShop");
                    saveItemStack(configuration, this.shopGuiPlusConfiguration, "buttons.goBack.item.", key);
                    break;
                case "previousPage":
                    configuration.set(zShopPath + "type", "PREVIOUS");
                    saveItemStack(configuration, this.shopGuiPlusConfiguration, "buttons.previousPage.item.", key);
                    break;
                case "nextPage":
                    configuration.set(zShopPath + "type", "NEXT");
                    saveItemStack(configuration, this.shopGuiPlusConfiguration, "buttons.nextPage.item.", key);
                    break;

                default:
                    Logger.info(key + " button not found with convert", Logger.LogType.WARNING);
                    break;
            }

            configuration.set(zShopPath + "slot", shopGuiPlusConfiguration.get(newPath + "slot", 0));
        }
    }

    private int saveItems(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path, String defaultType) {
        Set<String> keys = shopGuiPlusConfiguration.getConfigurationSection(path).getKeys(false);
        for (String key : keys) {
            try {
                saveItem(configuration, shopGuiPlusConfiguration, path + key + ".", key, defaultType);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return keys.size();
    }

    private void saveItem(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path, String key, String defaultType) {

        String zShopPath = "items." + key + ".";

        String type = shopGuiPlusConfiguration.getString(path + "type", defaultType).toLowerCase();

        switch (type) {
            case "item":

                configuration.set(zShopPath + "type", "ZSHOP_ITEM");
                saveItemStack(configuration, shopGuiPlusConfiguration, path + "item.", key);
                configuration.set(zShopPath + "buyPrice", shopGuiPlusConfiguration.getDouble(path + "buyPrice", 0.0));
                configuration.set(zShopPath + "sellPrice", shopGuiPlusConfiguration.getDouble(path + "sellPrice", 0.0));
                int page = shopGuiPlusConfiguration.getInt(path + "page", 1);
                if (page > 1) configuration.set(zShopPath + "page", page);

                break;

            case "shop_link":

                saveItemStack(configuration, shopGuiPlusConfiguration, path + "item.", key);

                String shopName = shopGuiPlusConfiguration.getString(path + "shop", "");
                if (shopName.isEmpty()) break;

                configuration.set(zShopPath + "type", "INVENTORY");
                configuration.set(zShopPath + "inventory", shopName);
                configuration.set(zShopPath + "plugin", "ZSHOP");

                break;

            case "dummy":
                saveItemStack(configuration, shopGuiPlusConfiguration, path + "item.", key);
                break;

            case "special":
                String special = shopGuiPlusConfiguration.getString(path + "special", "");
                if (special.equalsIgnoreCase("balance"))
                    saveItemStack(configuration, this.shopGuiPlusConfiguration, "specialElements.balance.item.", key);
                break;

            case "none":
                break;

            default:
                Logger.info(type + " is not supported with conversion !", Logger.LogType.WARNING);
                break;
        }

        configuration.set(zShopPath + "slot", shopGuiPlusConfiguration.get(path + "slot", 0));

        List<String> commands = shopGuiPlusConfiguration.getStringList(path + "commands");
        if (!commands.isEmpty()) configuration.set(zShopPath + "commands", commands);

        commands = shopGuiPlusConfiguration.getStringList(path + "commandsOnClick");
        if (!commands.isEmpty()) configuration.set(zShopPath + "commands", commands);

        List<String> consoleCommands = shopGuiPlusConfiguration.getStringList(path + "commandsOnClickConsole");
        if (!consoleCommands.isEmpty()) configuration.set(zShopPath + "consoleCommands", consoleCommands);
    }

    private void saveItemStack(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path, String key) {
        saveItemStack(configuration, shopGuiPlusConfiguration, path, key, null);
    }

    private void saveItemStack(YamlConfiguration configuration, YamlConfiguration shopGuiPlusConfiguration, String path, String key, String customPath) {

        String zShopPath = customPath == null ? "items." + key + ".item." : customPath;

        configuration.set(zShopPath + "material", shopGuiPlusConfiguration.getString(path + "material", "STONE"));

        if (shopGuiPlusConfiguration.contains(path + "itemsAdder")) {
            configuration.set(zShopPath + "material", "itemsadder:" + shopGuiPlusConfiguration.getString(path + "itemsAdder"));
        }

        if (shopGuiPlusConfiguration.contains(path + "skin")) {
            configuration.set(zShopPath + "material", null);
            configuration.set(zShopPath + "url", shopGuiPlusConfiguration.getString(path + "skin", null));
        }

        int amount = shopGuiPlusConfiguration.getInt(path + "quantity", 1);
        if (amount > 1) configuration.set(zShopPath + "amount", amount);

        configuration.set(zShopPath + "name", convertOldHexString(shopGuiPlusConfiguration.getString(path + "name", null)));
        configuration.set(zShopPath + "modelID", shopGuiPlusConfiguration.getString(path + "model", null));
        List<String> lore = shopGuiPlusConfiguration.getStringList(path + "lore");
        if (!lore.isEmpty())
            configuration.set(zShopPath + "lore", lore.stream().map(this::convertOldHexString).collect(Collectors.toList()));
        List<String> flags = shopGuiPlusConfiguration.getStringList(path + "flags");
        if (!flags.isEmpty()) configuration.set(zShopPath + "flags", flags);
        if (shopGuiPlusConfiguration.getBoolean(path + "glow")) configuration.set(zShopPath + "glow", true);

    }

    private String convertOldHexString(String string) {

        if (string == null) return null;

        Pattern pattern = Pattern.compile("&x[a-fA-F0-9-&]{12}");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String color = string.substring(matcher.start(), matcher.end());
            String colorReplace = color.replace("&x", "#");
            colorReplace = colorReplace.replace("&", "");
            string = string.replace(color, colorReplace);
            matcher = pattern.matcher(string);
        }

        return string;
    }

    private void loadMainConfig(File folder, CommandSender sender) {
        sender.sendMessage("§fStart convert §bconfig.yml");

        File file = new File(folder, "config.yml");
        if (!file.exists()) {
            sender.sendMessage("§f" + file.getPath() + " §cfile doesnt exist.");
            return;
        }

        this.shopGuiPlusConfiguration = YamlConfiguration.loadConfiguration(file);

        File newFile = new File(zShopFolderConvert, "shop.yml");

        if (newFile.exists()) {
            sender.sendMessage("§f" + newFile.getPath() + " already exist ! Stop convert " + file.getPath());
            return;
        }

        try {
            newFile.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(newFile);

        configuration.set("name", this.shopGuiPlusConfiguration.get("shopMenuName", "Default name"));
        configuration.set("size", this.shopGuiPlusConfiguration.get("shopMenuSize", 54));
        if (this.shopGuiPlusConfiguration.contains("shopMenuFillItem")) {
            saveItemStack(configuration, this.shopGuiPlusConfiguration, "shopMenuFillItem.", "", "fillItem.");
        }

        int size = saveItems(configuration, this.shopGuiPlusConfiguration, "shopMenuItems.", "shop_link");
        sender.sendMessage("§fConvert §e" + size + " §fitems");

        try {
            configuration.save(newFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        sender.sendMessage("§fEnd convert §bconfig.yml");
    }

}
