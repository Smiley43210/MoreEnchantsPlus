package me.adam561.mep2.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class Friendly extends CustomEnchantment implements Listener {
	public Friendly() {
		super("Friendly", "Friendly", ChatColor.LIGHT_PURPLE, new String[]{"LEGGINGS"});
	}
	
	@EventHandler
	public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent e) {
		if (!(e.getTarget() instanceof Player)) {
			return;
		}
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		Player p = (Player) e.getTarget();
		LivingEntity le = (LivingEntity) e.getEntity();
		if (le instanceof Wither || le instanceof WitherSkull || le instanceof EnderDragon) {
			return;
		}
		if (!this.itemHasEnchantment(p.getInventory().getLeggings())) {
			return;
		}
		if (le.getHealth() == le.getMaxHealth()) {
			e.setCancelled(true);
			e.setTarget(null);
		}
	}
	
	@Override
	public String userDescription() {
		return " Mobs only attack you when they have been attacked. (Not including bosses) ";
	}
	
	@Override
	public String userItems() {
		return "Leggings";
	}
}