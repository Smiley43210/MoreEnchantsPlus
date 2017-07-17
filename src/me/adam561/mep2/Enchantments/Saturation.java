package me.adam561.mep2.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Saturation extends CustomEnchantment implements Listener {
	public Saturation() {
		super("Saturation", "Saturation", ChatColor.DARK_RED, new String[]{"HELMET"});
	}
	
	@EventHandler
	public void onHungerEvent(FoodLevelChangeEvent e) {
		if (!this.itemHasEnchantment(e.getEntity().getInventory().getHelmet())) {
			return;
		}
		e.setCancelled(true);
		if (e.getEntity() instanceof Player) {
			((Player) e.getEntity()).setFoodLevel(20);
		} else {
			e.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 0));
		}
	}
	
	@Override
	public String userDescription() {
		return "Get automagically fed after eating any meal!";
	}
	
	@Override
	public String userItems() {
		return "Helmets";
	}
}