package me.adam561.mep2.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class WaterInfused extends CustomEnchantment implements Listener {
	public WaterInfused() {
		super("WaterInfused", "Water-Infused", ChatColor.AQUA, new String[]{"SWORD", "BOW"});
	}
	
	/*
	 * Enabled force condition propagation
	 * Lifted jumps to return sites
	 */
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Player p;
		if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) {
			return;
		}
		if (e.getDamager() instanceof Arrow) {
			ProjectileSource source = ((Arrow) e.getDamager()).getShooter();
			if (!(source instanceof Player)) {
				return;
			}
			p = (Player) source;
		} else {
			p = (Player) e.getDamager();
		}
		if (!this.itemHasEnchantment(p.getItemInHand())) {
			return;
		}
		if (!(e.getEntity() instanceof Blaze) && !(e.getEntity() instanceof Enderman)) {
			return;
		}
		e.setDamage((double) Math.round(e.getDamage() * (double) (this.getEnchantmentLevel(p.getItemInHand()) + 1)));
		e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation(), Effect.SMOKE, 5);
	}
	
	@Override
	public String userDescription() {
		return " Do more damage to Blazes and Endermen";
	}
	
	@Override
	public String userItems() {
		return "Swords, Bows";
	}
}

