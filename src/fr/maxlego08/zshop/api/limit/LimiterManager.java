package fr.maxlego08.zshop.api.limit;

import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface LimiterManager {

    Collection<Limit> getLimits();

    void create(Limit limit);

    void delete(Limit limit);

    void delete(int id);

    Optional<Limit> getLimit(int id);

    Optional<Limit> getLimit(UUID ownerUUID);

    Optional<Limit> getLimit(String material);

    void update(Limit limit);

    void deletes();

}
