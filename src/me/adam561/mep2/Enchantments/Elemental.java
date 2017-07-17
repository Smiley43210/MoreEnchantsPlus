package me.adam561.mep2.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class Elemental extends CustomEnchantment {
	public Elemental() {
		super("Elemental", "Elemental", ChatColor.GOLD, new String[]{"SWORD", "BOW"});
	}
	
	/*
	 * Enabled force condition propagation
	 * Lifted jumps to return sites
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Player p;
		if (!(e.getEntity() instanceof Rabbit || e.getEntity() instanceof Blaze || e.getEntity() instanceof MagmaCube || e.getEntity() instanceof Guardian || e.getEntity() instanceof Squid || e.getEntity() instanceof Bat || e.getEntity() instanceof Creeper || e.getEntity() instanceof Player || e.getEntity() instanceof Cow || e.getEntity() instanceof Chicken || e.getEntity() instanceof Sheep || e.getEntity() instanceof Ghast)) {
			return;
		}
		if (!(e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
			return;
		}
		EntityDamageByEntityEvent edev = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
		if (!(edev.getDamager() instanceof Player) && !(edev.getDamager() instanceof Arrow)) {
			return;
		}
		if (edev.getDamager() instanceof Arrow) {
			ProjectileSource source = ((Arrow) edev.getDamager()).getShooter();
			if (!(source instanceof Player)) {
				return;
			}
			p = (Player) source;
		} else {
			p = (Player) edev.getDamager();
		}
		if (!this.itemHasEnchantment(p.getInventory().getItemInMainHand())) {
			return;
		}
		int level = this.getEnchantmentLevel(p.getInventory().getItemInMainHand());
		LivingEntity entity = e.getEntity();
		int duration = level * 20;
		int amp = level / 2 + 1;
		if (entity instanceof Rabbit) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amp));
			return;
		} else if (entity instanceof Blaze) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration * 2, amp));
			return;
		} else if (entity instanceof MagmaCube) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, amp));
			return;
		} else if (entity instanceof Guardian) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, duration * 2, amp));
			return;
		} else if (entity instanceof Squid) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, (int) ((double) duration * 1.5), amp));
			return;
		} else if (entity instanceof Bat) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, amp));
			return;
		} else if (entity instanceof Ghast) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1));
			return;
		} else if (entity instanceof Creeper) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 2, amp));
			return;
		} else if (entity instanceof Player) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, amp / 2));
			return;
		} else {
			if (!(entity instanceof Cow) && !(entity instanceof Sheep) && !(entity instanceof Chicken)) {
				return;
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, duration, amp));
		}
	}
	
	@Override
	public String userDescription() {
		return "Gain a potion effects for killing mobs:\n Rabbit - Speed\n Player - Health\n Cow/Chicken/Sheep - Saturation\n Blaze/MagmaCube - Fire Resistance\n Guardian/Squid - Water Breathing\n Bat - Jump\n Ghast - Invisibility\n Creeper - Resistance";
	}
	
	@Override
	public String userItems() {
		return "Swords, Bows";
	}
}