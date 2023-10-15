package fr.maxlego08.zshop.history;

import fr.maxlego08.zshop.ShopPlugin;
import fr.maxlego08.zshop.api.history.History;
import fr.maxlego08.zshop.api.history.HistoryFile;
import fr.maxlego08.zshop.api.history.HistoryManager;
import fr.maxlego08.zshop.api.history.HistoryType;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.zcore.enums.Message;
import fr.maxlego08.zshop.zcore.utils.ZUtils;
import fr.maxlego08.zshop.zcore.utils.inventory.Pagination;
import fr.maxlego08.zshop.zcore.utils.storage.Persist;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ZHistoryManager extends ZUtils implements HistoryManager {

    private final Map<UUID, HistoryFile> histoies = new HashMap<>();
    private final ShopPlugin plugin;
    private final Persist persist;
    private final String folder = "logs/";

    public ZHistoryManager(ShopPlugin plugin, Persist persist) {
        super();
        this.plugin = plugin;
        this.persist = persist;
        File folder = new File(plugin.getDataFolder(), this.folder);
        if (!folder.exists()) folder.mkdir();
    }

    @Override
    public Optional<HistoryFile> getFile(UUID uuid) {
        HistoryFile value = this.histoies.getOrDefault(uuid, null);
        return value == null ? Optional.empty() : Optional.of(value);
    }

    @Override
    public HistoryFile getOrCreate(UUID uuid) {
        Optional<HistoryFile> optional = getFile(uuid);
        return optional.orElseGet(() -> loadOrCreate(uuid));
    }

    @Override
    public HistoryFile loadOrCreate(UUID uuid) {
        File file = new File(this.plugin.getDataFolder(), this.folder + uuid + ".json");
        ZHistoryFile historyFile = new ZHistoryFile(uuid);
        historyFile = persist.loadOrSaveDefault(historyFile, ZHistoryFile.class, file, false);
        historyFile.clear();
        this.histoies.put(uuid, historyFile);
        return historyFile;
    }

    @Override
    public boolean isLoad(UUID uuid) {
        return getFile(uuid).isPresent();
    }

    @Override
    public void asyncSave() {
        runAsync(this.plugin, () -> {
            histoies.values().forEach(value -> {
                UUID uuid = value.getUniqueId();
                File file = new File(plugin.getDataFolder(), this.folder + uuid + ".json");
                persist.save(value, file, false);
            });
        });
    }

    @Override
    public void asyncSave(UUID uuid) {
        runAsync(this.plugin, () -> {
            Optional<HistoryFile> optional = getFile(uuid);
            if (optional.isPresent()) {
                File file = new File(plugin.getDataFolder(), this.folder + uuid + ".json");
                persist.save(optional.get(), file, false);
            }
        });
    }

    @Override
    public void asyncValue(UUID uuid, History history) {
        runAsync(this.plugin, () -> {
            HistoryFile file = getOrCreate(uuid);
            file.addHistory(history);
            if (file.canSave()) {
                file.setSavedAt();
                this.save(uuid);
            }
        });
    }

    @Override
    public void readLogs(CommandSender sender, OfflinePlayer offlinePlayer, HistoryType type, int page) {
        runAsync(this.plugin, () -> {
            File file = new File(this.plugin.getDataFolder(), this.folder + offlinePlayer.getUniqueId() + ".json");
            if (!file.exists()) {
                message(this.plugin, sender, Message.LOG_INFO_NONE, "%player%", offlinePlayer.getName());
                return;
            }

            HistoryFile historyFile = loadOrCreate(offlinePlayer.getUniqueId());
            List<History> histories = historyFile.getHistories().stream().filter(e -> e.getType() == type).sorted(Comparator.comparingLong(History::getDate).reversed()).collect(Collectors.toList());
            int maxPage = getMaxPage(histories, 10);
            messageWO(this.plugin, sender, Message.LOG_INFO_HEADER, "%player%", offlinePlayer.getName(), "%page%", page, "%maxPage%", maxPage, "%type%", type.name());

            Pagination<History> pagination = new Pagination<>();
            pagination.paginate(histories, 10, page).forEach(history -> {
                messageWO(this.plugin, sender, Message.LOG_INFO_MESSAGE, "%date%", history.getDateAsString(), "%message%", history.getMessage());
            });

            messageWO(this.plugin, sender, Message.LOG_INFO_FOOTER, "%player%", offlinePlayer.getName());
        });
    }

    @Override
    public void save(UUID uuid) {
        Optional<HistoryFile> optional = getFile(uuid);
        if (optional.isPresent()) {
            File file = new File(plugin.getDataFolder(), this.folder + uuid + ".json");
            persist.save(optional.get(), file, false);
        }
    }

    @EventHandler
    public void onQuid(PlayerQuitEvent event) {
        this.histoies.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Config.enableItemLog && Config.enableItemLogInFile) {
            runAsync(this.plugin, () -> {
                UUID uuid = event.getPlayer().getUniqueId();
                File file = new File(this.plugin.getDataFolder(), this.folder + uuid + ".json");
                if (file.exists()) this.loadOrCreate(uuid);
            });
        }
    }
}
