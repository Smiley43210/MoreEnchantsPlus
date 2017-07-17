package me.adam561.mep2.Enchantments;

import me.adam561.mep2.ConfigData;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

public enum MEPEnchantment {
	ELEMENTAL(new Elemental()),
	KNOWLEDGE_INFUSED(new Knowledge()),
	WATER_INFUSED(new WaterInfused()),
	PROTECTIVE(new Protective()),
	SPEED(new Speed()),
	FRIENDLY(new Friendly()),
	SATURATION(new Saturation()),
	POISON(new Potion(PotionEffectType.POISON, "Poison", ChatColor.DARK_GREEN, new ConfigData("Poison"))),
	WITHER(new Potion(PotionEffectType.WITHER, "Wither", ChatColor.DARK_PURPLE, new ConfigData("Wither"))),
	ICE(new Potion(PotionEffectType.SLOW, "Ice", ChatColor.BLUE, new ConfigData("Ice"))),
	FIRE_INFUSED(new FireInfused());
	
	private CustomEnchantment enchantment;
	private static final HashMap<CustomEnchantment, MEPEnchantment> lookup;
	
	private MEPEnchantment(CustomEnchantment e) {
		this.enchantment = e;
	}
	
	public CustomEnchantment enchantment() {
		return this.enchantment;
	}
	
	public static MEPEnchantment get(CustomEnchantment e) {
		return lookup.get(e);
	}
	
	public static ArrayList<CustomEnchantment> enchantments() {
		ArrayList<CustomEnchantment> enchs = new ArrayList<CustomEnchantment>();
		for (MEPEnchantment e : EnumSet.allOf(MEPEnchantment.class)) {
			enchs.add(e.enchantment());
		}
		return enchs;
	}
	
	static {
		lookup = new HashMap<CustomEnchantment, MEPEnchantment>();
		for (MEPEnchantment ench : EnumSet.allOf(MEPEnchantment.class)) {
			lookup.put(ench.enchantment(), ench);
		}
	}
}