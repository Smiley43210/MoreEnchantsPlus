package me.adam561.mep2.Enchantments;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Random;

public class FireInfused extends CustomEnchantment implements Listener {
	public FireInfused() {
		super("FireInfused", "Fire-Infused", ChatColor.DARK_RED, new String[]{"PICKAXE", "SWORD", "BOW"});
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		int n;
		Player p = e.getPlayer();
		if (p.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		if (!this.itemHasEnchantment(p.getInventory().getItemInMainHand())) {
			return;
		}
		Block b = e.getBlock();
		Material ingot = Material.AIR;
		if (b.getType().equals(Material.IRON_ORE)) {
			ingot = Material.IRON_INGOT;
		} else if (b.getType().equals(Material.GOLD_ORE)) {
			ingot = Material.GOLD_INGOT;
		}
		if (ingot.equals(Material.AIR)) {
			return;
		}
		if (e.getPlayer().getInventory().getItemInMainHand().getEnchantments().toString().contains("LOOT_BONUS_BLOCKS")) {
			int level = e.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			Random rand = new Random();
			if (level == 1) {
				level = 2;
			}
			n = rand.nextInt(level) + 1;
		} else {
			n = 1;
		}
		e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(ingot, n));
		e.getBlock().setType(Material.AIR);
		e.getBlock().getLocation().getWorld().playEffect(e.getBlock().getLocation(), Effect.SMOKE, 100);
		e.getBlock().getLocation().getWorld().playEffect(e.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 50);
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
		if (!this.itemHasEnchantment(p.getInventory().getItemInMainHand())) {
			return;
		}
		if (!(e.getEntity() instanceof Guardian) && !(e.getEntity() instanceof Squid)) {
			return;
		}
		e.setDamage((double) Math.round(e.getDamage() * (double) (this.getEnchantmentLevel(p.getInventory().getItemInMainHand()) + 1)));
		e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
	}
	
	@Override
	public String userDescription() {
		return " Pickaxe - Automatically smelt Gold and Iron\n Sword/Bow - Do more damage to Squids and Guardians";
	}
	
	@Override
	public String userItems() {
		return "Swords, Bows, Pickaxes";
	}
}