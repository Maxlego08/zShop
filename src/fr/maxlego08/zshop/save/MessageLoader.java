package fr.maxlego08.zshop.save;

import fr.maxlego08.zshop.zcore.enums.Message;
import fr.maxlego08.zshop.zcore.enums.MessageType;
import fr.maxlego08.zshop.zcore.logger.Logger;
import fr.maxlego08.zshop.zcore.utils.nms.NMSUtils;
import fr.maxlego08.zshop.zcore.utils.storage.Persist;
import fr.maxlego08.zshop.zcore.utils.storage.Saveable;
import fr.maxlego08.zshop.zcore.utils.yaml.YamlUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageLoader extends YamlUtils implements Saveable {

    private final List<Message> loadedMessages = new ArrayList<>();

    public MessageLoader(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void save(Persist persist) {

        if (persist != null) return;

        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        YamlConfiguration configuration = getConfig(file);
        for (Message message : Message.values()) {

            if (!message.isUse()) continue;

            String path = "messages." + message.name().toLowerCase().replace("_", ".");

            if (message.getType() != MessageType.TCHAT) {
                configuration.set(path + ".type", message.getType().name());
            }

            if (message.getType().equals(MessageType.TCHAT) || message.getType().equals(MessageType.ACTION) || message.getType().equals(MessageType.CENTER)) {

                if (message.isMessage()) {
                    configuration.set(path + ".messages", colorReverse(message.getMessages()));
                } else {
                    configuration.set(path + ".message", colorReverse(NMSUtils.isHexColor() && message.getMessageNewVersion() != null ? message.getMessageNewVersion() : message.getMessage()));
                }

            } else if (message.getType().equals(MessageType.TITLE)) {

                configuration.set(path + ".title", colorReverse(message.getTitle()));
                configuration.set(path + ".subtitle", colorReverse(message.getSubTitle()));
                configuration.set(path + ".fadeInTime", message.getStart());
                configuration.set(path + ".showTime", message.getTime());
                configuration.set(path + ".fadeOutTime", message.getEnd());

            }

        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load(Persist persist) {

        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            this.save(null);
            return;
        }

        YamlConfiguration configuration = getConfig(file);

        if (!configuration.contains("messages")) {
            this.save(null);
            return;
        }

        this.loadedMessages.clear();

        for (String key : configuration.getConfigurationSection("messages.").getKeys(false)) {
            loadMessage(configuration, "messages." + key);
        }

        boolean canSave = false;
        for (Message message : Message.values()) {

            if (!message.isValid()) {
                Logger.info("Error with message " + message + ", it is invalid!");
            }

            if (!this.loadedMessages.contains(message)) {
                canSave = true;
                break;
            }
        }

        // Allows you to save new parameters
        if (canSave) {
            Logger.info("Save the message file, add new settings");
            this.save(null);
        }
    }

    /**
     * @param configuration
     * @param key
     */
    private void loadMessage(YamlConfiguration configuration, String key) {

        try {
            MessageType messageType = MessageType.valueOf(configuration.getString(key + ".type", "TCHAT").toUpperCase());
            String keys = key.substring("messages.".length());
            Message enumMessage = Message.valueOf(keys.toUpperCase().replace(".", "_"));
            enumMessage.setType(messageType);
            loadedMessages.add(enumMessage);

            switch (messageType) {
                case ACTION: {
                    String message = configuration.getString(key + ".message");
                    enumMessage.setMessage(message);
                    break;
                }
                case CENTER:
                case TCHAT: {
                    if (configuration.contains(key + ".messages")) {
                        List<String> messages = configuration.getStringList(key + ".messages");
                        enumMessage.setMessages(messages);
                        enumMessage.setMessage(null);
                    } else {
                        String message = configuration.getString(key + ".message");
                        enumMessage.setMessage(message);
                        enumMessage.setMessages(new ArrayList<>());
                    }
                    break;
                }
                case TITLE: {
                    String title = configuration.getString(key + ".title");
                    String subtitle = configuration.getString(key + ".subtitle");
                    int fadeInTime = configuration.getInt(key + ".fadeInTime");
                    int showTime = configuration.getInt(key + ".showTime");
                    int fadeOutTime = configuration.getInt(key + ".fadeOutTime");
                    Map<String, Object> titles = new HashMap<>();
                    titles.put("title", title);
                    titles.put("subtitle", subtitle);
                    titles.put("start", fadeInTime);
                    titles.put("time", showTime);
                    titles.put("end", fadeOutTime);
                    titles.put("isUse", true);
                    enumMessage.setTitles(titles);
                    break;
                }
                default:
                    break;
            }

        } catch (Exception ignored) {
        }

        try {
            for (String newKey : configuration.getConfigurationSection(key + ".").getKeys(false)) {
                loadMessage(configuration, key + "." + newKey);
            }
        } catch (Exception ignored) {
        }
    }

}
