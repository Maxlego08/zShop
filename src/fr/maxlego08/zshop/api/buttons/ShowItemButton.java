package fr.maxlego08.zshop.api.buttons;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.zshop.api.economy.ShopEconomy;
import org.bukkit.entity.Player;

import java.util.List;

public interface ShowItemButton extends Button {

    List<String> getLore();

}
