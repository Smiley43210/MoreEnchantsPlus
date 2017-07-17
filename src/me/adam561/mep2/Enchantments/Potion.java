package me.adam561.mep2.Enchantments;

import me.adam561.mep2.ConfigData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class Potion extends CustomEnchantment implements Listener {
	private PotionEffectType type;
	private String displayName;
	
	public Potion(PotionEffectType type, String displayName, ChatColor color, ConfigData data) {
		super(data.getConfigID(), displayName, color, new String[]{"SWORD", "BOW"});
		this.type = type;
		this.displayName = displayName;
	}
	
	/*
	 * Enabled force condition propagation
	 * Lifted jumps to return sites
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Player p;
		if (e.isCancelled()) {
			return;
		}
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
		if (!this.itemHasEnchantment(p.getInventory().getItemInMainHand())) {
			return;
		}
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		if (e.getEntity() instanceof Player) {
			Player play = (Player) e.getEntity();
			Protective pro = (Protective) MEPEnchantment.PROTECTIVE.enchantment();
			if (pro.itemHasEnchantment(play.getInventory().getChestplate())) {
				return;
			}
		}
		if (e.getDamage() <= 0.0) {
			return;
		}
		((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(this.type, 20 * this.getEnchantmentLevel(p.getInventory().getItemInMainHand()), 1));
	}
	
	@Override
	public String userDescription() {
		return "Inflicts " + this.displayName + " level II any enemy hit";
	}
	
	@Override
	public String userItems() {
		return "Swords, Bows";
	}
}