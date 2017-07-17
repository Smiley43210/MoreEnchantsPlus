/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.adam561.mep2.Enchantments;

import me.adam561.mep2.ConfigData;
import me.adam561.mep2.RomanNumerals;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomEnchantment implements Listener {
	private String name;
	private String congfigID;
	private ChatColor color;
	private ConfigData data;
	private String[] permittedItems;
	
	public CustomEnchantment(String configID, String name, ChatColor color, String[] permittedItems) {
		this.congfigID = configID;
		this.name = name;
		this.color = color;
		this.data = new ConfigData(configID);
		this.permittedItems = permittedItems;
	}
	
	public boolean enchantItem(ItemStack itemToEnchant, int level) {
		if (level > this.getMaxLevel()) {
			return false;
		}
		if (!this.itemIsPermitted(itemToEnchant)) {
			return false;
		}
		return this.enchant(itemToEnchant, level);
	}
	
	public boolean forceEnchantItem(ItemStack itemToEnchant, int level) {
		if (itemToEnchant.getType().equals(Material.AIR)) {
			return false;
		}
		return this.enchant(itemToEnchant, level);
	}
	
	private boolean enchant(ItemStack itemToEnchant, int level) {
		String roman = "";
		if (level > 0) {
			roman = " " + RomanNumerals.getRoman(level);
		}
		if (!itemToEnchant.hasItemMeta()) {
			ItemMeta im = itemToEnchant.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(this.color + this.name + roman);
			im.setLore(lore);
			itemToEnchant.setItemMeta(im);
			return true;
		}
		ItemMeta im = itemToEnchant.getItemMeta();
		if (im.getLore().toString().contains(this.color + this.name)) {
			List<String> lore = im.getLore();
			for (String s : lore) {
				if (!s.contains(this.color + this.name)) {
					continue;
				}
				lore.set(lore.indexOf(s), this.color + this.name + roman);
				break;
			}
			im.setLore(lore);
			itemToEnchant.setItemMeta(im);
			return true;
		}
		List<String> lore = im.getLore();
		lore.add(this.color + this.name + roman);
		im.setLore(lore);
		itemToEnchant.setItemMeta(im);
		return true;
	}
	
	public boolean itemHasEnchantment(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().toString().contains(this.color + this.name);
	}
	
	public boolean itemIsPermitted(ItemStack item) {
		for (String material : this.permittedItems) {
			if (!item.getType().toString().contains(material)) {
				continue;
			}
			return true;
		}
		return false;
	}
	
	public int getEnchantmentLevel(ItemStack i) {
		if (!this.itemHasEnchantment(i)) {
			return 0;
		}
		String name = this.getEnchantmentString(i);
		String[] parts = name.split(" ");
		return RomanNumerals.getNumber(parts[parts.length - 1]);
	}
	
	private String getEnchantmentString(ItemStack i) {
		for (String s : i.getItemMeta().getLore()) {
			if (!s.contains(this.color + this.name)) {
				continue;
			}
			return s;
		}
		return "";
	}
	
	public static HashMap<CustomEnchantment, Integer> getEnchantments(ItemStack item) {
		HashMap<CustomEnchantment, Integer> enchs = new HashMap<CustomEnchantment, Integer>();
		for (CustomEnchantment ench : MEPEnchantment.enchantments()) {
			if (!ench.itemHasEnchantment(item)) {
				continue;
			}
			enchs.put(ench, ench.getEnchantmentLevel(item));
		}
		return enchs;
	}
	
	public String userItems() {
		return "Not Found";
	}
	
	public String userDescription() {
		return "Not Found";
	}
	
	public String getCongfigID() {
		return this.congfigID;
	}
	
	public int getMinExp() {
		return this.data.min_exp_cost;
	}
	
	public int getChance() {
		return this.data.chance;
	}
	
	public int getMaxLevel() {
		return this.data.max_level;
	}
	
	public String getName() {
		return this.color + this.name;
	}
	
	public void cleanUp() {
	}
	
	public String toString() {
		return this.name;
	}
}

