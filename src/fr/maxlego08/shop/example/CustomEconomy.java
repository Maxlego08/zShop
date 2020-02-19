package fr.maxlego08.shop.example;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.maxlego08.shop.event.events.economy.EconomyCurrencyEvent;
import fr.maxlego08.shop.event.events.economy.EconomyDepositEvent;
import fr.maxlego08.shop.event.events.economy.EconomyHasMoneyEvent;
import fr.maxlego08.shop.event.events.economy.EconomyWithdrawMoney;

public class CustomEconomy implements Listener {

	@EventHandler
	public void canSell(EconomyHasMoneyEvent event){
		Player player = event.getPlayer();
		//Your method to check if player money
		event.setHasMoney(true);
	}
	
	@EventHandler
	public void currencyEvent(EconomyCurrencyEvent event){
		event.setCurrency("you currency");
	}
	
	@EventHandler
	public void depositeEvent(EconomyDepositEvent event){
		Player player = event.getPlayer();
		double value = event.getMoney();
		//Your method of giving money to a player
	}
	
	@EventHandler
	public void withdrawEvent(EconomyWithdrawMoney event){
		Player player = event.getPlayer();
		double value = event.getMoney();
		//Your method of withdrawing money from a player
	}
	
}
