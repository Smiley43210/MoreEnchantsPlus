/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 */
package me.adam561.mep2;

public class ConfigData {
	public int min_exp_cost;
	public int chance;
	public int max_level;
	public String configID;
	
	public ConfigData(String configID) {
		this.configID = configID;
		this.chance = Main.getPlugin().getConfig().getInt("chance_for_" + configID);
		this.min_exp_cost = Main.getPlugin().getConfig().getInt("min_exp_cost_for_" + configID);
		this.max_level = Main.getPlugin().getConfig().getInt("max_level_for_" + configID);
	}
	
	public int getMin_exp_cost() {
		return this.min_exp_cost;
	}
	
	public int getChance() {
		return this.chance;
	}
	
	public int getMax_level() {
		return this.max_level;
	}
	
	public String getConfigID() {
		return this.configID;
	}
}

