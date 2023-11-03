package fr.maxlego08.zshop.api.buttons;

import fr.maxlego08.menu.api.button.Button;
import org.bukkit.entity.Player;

public interface EconomyAction extends Button {

    void buy(Player player, int amount);

    void sell(Player player, int amount);

}
