package me.adam561.mep2;

import me.adam561.mep2.Enchantments.MEPEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Alters implements Listener {
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		Block beacon;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (e.getItem() == null) {
			return;
		}
		if (!MEPEnchantment.KNOWLEDGE_INFUSED.enchantment().itemIsPermitted(e.getItem())) {
			return;
		}
		int level = MEPEnchantment.KNOWLEDGE_INFUSED.enchantment().getEnchantmentLevel(e.getItem());
		if (level >= MEPEnchantment.KNOWLEDGE_INFUSED.enchantment().getMaxLevel()) {
			return;
		}
		if (e.getClickedBlock().getType().equals(Material.DIAMOND_BLOCK)) {
			level += 2;
		} else if (e.getClickedBlock().getType().equals(Material.GOLD_BLOCK)) {
			++level;
		} else {
			return;
		}
		Material toEnd = Material.AIR;
		if (level > MEPEnchantment.KNOWLEDGE_INFUSED.enchantment().getMaxLevel()) {
			level = MEPEnchantment.KNOWLEDGE_INFUSED.enchantment().getMaxLevel();
			if (e.getClickedBlock().getType().equals(Material.DIAMOND_BLOCK)) {
				toEnd = Material.GOLD_BLOCK;
			}
		}
		if (!(beacon = e.getClickedBlock().getRelative(BlockFace.DOWN)).getType().equals(Material.BEACON)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.DOWN).getType().equals(Material.BEDROCK)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.NORTH).getType().equals(Material.STAINED_GLASS)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.SOUTH).getType().equals(Material.STAINED_GLASS)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.EAST).getType().equals(Material.STAINED_GLASS)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.WEST).getType().equals(Material.STAINED_GLASS)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.NORTH_EAST).getType().equals(Material.IRON_BLOCK)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.IRON_BLOCK)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.IRON_BLOCK)) {
			return;
		}
		if (!beacon.getRelative(BlockFace.NORTH_WEST).getType().equals(Material.IRON_BLOCK)) {
			return;
		}
		MEPEnchantment.KNOWLEDGE_INFUSED.enchantment().forceEnchantItem(e.getItem(), level);
		e.getClickedBlock().setType(toEnd);
		e.getClickedBlock().getWorld().strikeLightningEffect(e.getClickedBlock().getLocation());
	}
}

