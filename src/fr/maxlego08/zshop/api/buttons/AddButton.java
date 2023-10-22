package fr.maxlego08.zshop.api.buttons;

import fr.maxlego08.menu.api.button.Button;
import org.bukkit.entity.Player;

public interface AddButton extends Button {

    String getAmount();

    int parseInt(Player player);

}
