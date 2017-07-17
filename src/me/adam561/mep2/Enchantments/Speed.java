package me.adam561.mep2.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class Speed extends CustomEnchantment implements Listener {
	private static HashMap<UUID, Float> runners = new HashMap<UUID, Float>();
	
	public Speed() {
		super("Speed", "Speed", ChatColor.DARK_AQUA, new String[]{"BOW", "BOOTS"});
	}
	
	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!this.itemHasEnchantment(e.getBow())) {
			return;
		}
		e.getProjectile().setVelocity(e.getProjectile().getVelocity().multiply(this.getEnchantmentLevel(e.getBow()) + 1));
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockY() == e.getFrom().getBlockY() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
			return;
		}
		Player p = e.getPlayer();
		if (this.itemHasEnchantment(p.getInventory().getBoots())) {
			if (!runners.containsKey(p.getUniqueId())) {
				runners.put(p.getUniqueId(), Float.valueOf(p.getWalkSpeed()));
				float speed = p.getWalkSpeed() + (float) (this.getEnchantmentLevel(p.getInventory().getBoots()) + 1) * (p.getWalkSpeed() / 2.0f);
				if (speed > 1.0f) {
					speed = 1.0f;
				}
				p.setWalkSpeed(speed);
			}
		} else if (runners.containsKey(p.getUniqueId())) {
			p.setWalkSpeed(runners.get(p.getUniqueId()).floatValue());
			runners.remove(p.getUniqueId());
		}
	}
	
	@Override
	public void cleanUp() {
		for (UUID uuid : runners.keySet()) {
			Bukkit.getPlayer(uuid).setWalkSpeed(runners.get(uuid).floatValue());
		}
		runners.clear();
	}
	
	@Override
	public String userDescription() {
		return " Bows - Speeds up arrows\n Boots - Increases walk/run speed";
	}
	
	@Override
	public String userItems() {
		return "Bows, Boots";
	}
}

