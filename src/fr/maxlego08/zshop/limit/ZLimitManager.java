package fr.maxlego08.zshop.limit;

import fr.maxlego08.zshop.api.limit.Limit;
import fr.maxlego08.zshop.api.limit.LimiterManager;
import fr.maxlego08.zshop.placeholder.LocalPlaceholder;
import fr.maxlego08.zshop.save.Config;
import fr.maxlego08.zshop.zcore.utils.storage.Persist;
import fr.maxlego08.zshop.zcore.utils.storage.Saveable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ZLimitManager implements LimiterManager, Saveable {

    public static List<ZLimit> limits = new ArrayList<>();

    public void registerPlaceholders() {

        LocalPlaceholder localPlaceholder = LocalPlaceholder.getInstance();
        localPlaceholder.register("limiter_time_", (player, args) -> {
            Optional<Limit> optional = getLimit(args);
            if (optional.isPresent()) {
                Limit limit = optional.get();
                limit.update();
                return limit.getFormattedTimeUntilNextTask();
            }
            return args + " not found";
        });
        localPlaceholder.register("limiter_date_", (player, args) -> {
            Optional<Limit> optional = getLimit(args);
            if (optional.isPresent()) {
                Limit limit = optional.get();
                limit.update();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Config.dateFormat);
                return simpleDateFormat.format(limit.getCalendar().getTime());
            }
            return args + " not found";
        });

    }

    @Override
    public void save(Persist persist) {
        persist.save(this, "limits");
    }

    @Override
    public void load(Persist persist) {
        persist.loadOrSaveDefault(this, ZLimitManager.class, "limits");
    }

    @Override
    public Collection<Limit> getLimits() {
        return null;
    }

    @Override
    public void create(Limit limit) {

        Optional<Limit> optional = getLimit(limit.getMaterial());
        ZLimit zLimit = (ZLimit) limit;
        zLimit.setValid(true);

        // Supprimer l'ancienne valeur et mettre à jour le nombre
        optional.ifPresent(value -> {
            limits.remove((ZLimit) value);
            zLimit.setAmount(value.getAmount());
        });

        limits.add(zLimit);
    }

    @Override
    public void delete(Limit limit) {
        limits.removeIf(e -> e.getMaterial().equals(limit.getMaterial()));
    }

    @Override
    public void delete(int id) {
        limits.removeIf(e -> e.getId() == id);
    }

    @Override
    public Optional<Limit> getLimit(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Limit> getLimit(UUID ownerUUID) {
        return Optional.empty();
    }

    @Override
    public Optional<Limit> getLimit(String material) {
        return limits.stream().filter(e -> e.getMaterial().equalsIgnoreCase(material)).map(e -> (Limit) e).findFirst();
    }

    @Override
    public void update(Limit limit) {

    }

    @Override
    public void deletes() {
        limits.removeIf(limit -> !limit.isValid());
    }
}
