package me.adam561.mep2.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class Knowledge extends CustomEnchantment implements Listener {
	public Knowledge() {
		super("Knowledge", "Knowledge-Infused", ChatColor.GREEN, new String[]{"SWORD", "PICKAXE", "BOW"});
	}
	
	/*
	 * Enabled force condition propagation
	 * Lifted jumps to return sites
	 */
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		Player p;
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
		e.setDroppedExp(e.getDroppedExp() * (this.getEnchantmentLevel(p.getInventory().getItemInMainHand()) + 1));
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if (!this.itemHasEnchantment(e.getPlayer().getInventory().getItemInMainHand())) {
			return;
		}
		e.setExpToDrop(e.getExpToDrop() * (this.getEnchantmentLevel(e.getPlayer().getInventory().getItemInMainHand()) + 1));
	}
	
	@Override
	public boolean enchantItem(ItemStack itemToEnchant, int level) {
		return false;
	}
	
	@Override
	public int getMinExp() {
		return 0;
	}
	
	@Override
	public int getChance() {
		return 0;
	}
	
	@Override
	public String userDescription() {
		return "Gain more EXP for killing mobs.\n Only obtainable via altars. \n '/mep altar' for help.";
	}
	
	@Override
	public String userItems() {
		return "Swords, Bows, Pickaxes";
	}
}