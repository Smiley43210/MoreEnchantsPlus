package me.adam561.mep2.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Protective extends CustomEnchantment implements Listener {
	public Protective() {
		super("MagicResistant", "Magic-Resistant", ChatColor.LIGHT_PURPLE, new String[]{"CHESTPLATE"});
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!this.itemHasEnchantment(((Player) e.getEntity()).getInventory().getChestplate())) {
			return;
		}
		if (e.getCause().equals(EntityDamageEvent.DamageCause.POISON) || e.getCause().equals(EntityDamageEvent.DamageCause.WITHER) || e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)) {
			e.setDamage(0.0);
			e.setCancelled(true);
		}
	}
	
	@Override
	public String userDescription() {
		return " Negates all damaging potion effects - Damage, Poison, Wither";
	}
	
	@Override
	public String userItems() {
		return "Chestplates";
	}
}