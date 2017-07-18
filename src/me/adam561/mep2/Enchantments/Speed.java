package me.adam561.mep2.Enchantments;

import me.adam561.mep2.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

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
	public void onPlayerJoin(PlayerJoinEvent e) {
		checkPlayerBoots(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		UUID playerUUID = player.getUniqueId();
		
		if (runners.containsKey(playerUUID)) {
			player.setWalkSpeed(runners.get(playerUUID));
			runners.remove(playerUUID);
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockY() == e.getFrom().getBlockY() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
			return;
		}
		
		checkPlayerBoots(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		checkPlayerBoots(e.getPlayer());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		InventoryHolder holder = e.getInventory().getHolder();
		
		if (holder instanceof Player) {
			checkPlayerBoots((Player) holder);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		InventoryHolder holder = e.getInventory().getHolder();
		
		if (holder instanceof Player) {
			checkPlayerBoots((Player) holder);
		}
	}
	
	private void checkPlayerBoots(Player player) {
		(new BukkitRunnable() {
			@Override
			public void run() {
				if (itemHasEnchantment(player.getInventory().getBoots())) {
					if (!runners.containsKey(player.getUniqueId())) {
						runners.put(player.getUniqueId(), player.getWalkSpeed());
						float speed = player.getWalkSpeed() + (float) (getEnchantmentLevel(player.getInventory().getBoots()) + 1) * (player.getWalkSpeed() / 2.0f);
						
						if (speed > 1.0f) {
							speed = 1.0f;
						}
						
						player.setWalkSpeed(speed);
					}
				} else if (runners.containsKey(player.getUniqueId())) {
					player.setWalkSpeed(runners.get(player.getUniqueId()));
					runners.remove(player.getUniqueId());
				}
			}
		}).runTask(Main.getPlugin());
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