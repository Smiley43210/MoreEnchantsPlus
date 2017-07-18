package me.adam561.mep2;

import me.adam561.mep2.Enchantments.CustomEnchantment;
import me.adam561.mep2.Enchantments.Knowledge;
import me.adam561.mep2.Enchantments.MEPEnchantment;
import me.adam561.mep2.VersionManager.Metrics;
import me.adam561.mep2.VersionManager.Updater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {
	private static Plugin plugin = null;
	public static boolean update = false;
	public static String name = "";
	public static Updater.ReleaseType type = null;
	public static String version = "";
	public static String link = "";
	private ArrayList<CustomEnchantment> enchants = new ArrayList<CustomEnchantment>();
	
	public void onEnable() {
		if (plugin == null) {
			plugin = this;
		}
		
		this.initConfig();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new Altars(), this);
		
		if (this.getConfig().getBoolean("canUpdate")) {
			Updater updater = new Updater(this, 79543, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
			name = updater.getLatestName();
			version = updater.getLatestGameVersion();
			type = updater.getLatestType();
			link = updater.getLatestFileLink();
		}
		
		if (this.getConfig().getBoolean("metrics")) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				// empty catch block
			}
		}
		
		this.initEnchantments();
	}
	
	private void initEnchantments() {
		for (CustomEnchantment ench : MEPEnchantment.enchantments()) {
			this.enchants.add(ench);
			Bukkit.getServer().getPluginManager().registerEvents(ench, Main.getPlugin());
		}
	}
	
	private void initConfig() {
		this.saveDefaultConfig();
	}
	
	public void onDisable() {
		plugin = null;
		for (CustomEnchantment en : this.enchants) {
			en.cleanUp();
		}
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	@EventHandler
	public void onEnhantmentEvent(EnchantItemEvent e) {
		if (!e.getEnchanter().hasPermission("mep.enchant")) {
			return;
		}
		for (CustomEnchantment en : this.enchants) {
			if (en instanceof Knowledge) {
				continue;
			}
			Random rand = new Random();
			int cost = en.getMinExp();
			if (e.getExpLevelCost() < cost) {
				continue;
			}
			int chance = en.getChance();
			int result = rand.nextInt(100);
			if (result > chance) {
				continue;
			}
			float step = 30 / en.getMaxLevel();
			int levl = rand.nextInt(2);
			int finalLevel = (int) Math.floor((float) e.getExpLevelCost() / step);
			if (levl == 1) {
				--finalLevel;
			}
			if (finalLevel <= 0) {
				finalLevel = 1;
			}
			en.enchantItem(e.getItem(), finalLevel);
		}
	}
	
	public void displayHelp(Player p) {
		p.sendMessage("");
		p.sendMessage(ChatColor.GOLD + " --- More Enchants+ " + this.getDescription().getVersion() + " Commands --- ");
		p.sendMessage(ChatColor.GRAY + "/" + ChatColor.GREEN + "mep" + ChatColor.DARK_GREEN + " help");
		p.sendMessage(ChatColor.GRAY + "    - Show this dialogue");
		p.sendMessage(ChatColor.GRAY + "/" + ChatColor.GREEN + "mep" + ChatColor.DARK_GREEN + " altar");
		p.sendMessage(ChatColor.GRAY + "    - View tutorials for building Knowledge-Infused altars");
		p.sendMessage(ChatColor.GRAY + "/" + ChatColor.GREEN + "mep" + ChatColor.DARK_GREEN + " <enchantment>");
		p.sendMessage(ChatColor.GRAY + "    - Get enchantment info");
		p.sendMessage(ChatColor.GRAY + "/" + ChatColor.GREEN + "mep" + ChatColor.DARK_GREEN + " list");
		p.sendMessage(ChatColor.GRAY + "    - Show list of enchantments");
		if (p.hasPermission("mep.command")) {
			p.sendMessage(ChatColor.GRAY + "/" + ChatColor.GREEN + "mep" + ChatColor.DARK_GREEN + " <enchantment> <level>");
			p.sendMessage(ChatColor.GRAY + "    - Enchant item with level");
		}
		p.sendMessage(ChatColor.GOLD + " Version: " + ChatColor.GREEN + this.getDescription().getVersion());
		p.sendMessage(ChatColor.GOLD + " Author:  " + ChatColor.GREEN + (String) this.getDescription().getAuthors().get(0));
		p.sendMessage(ChatColor.GOLD + " Website: " + ChatColor.GREEN + this.getDescription().getWebsite());
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (command.getName().equalsIgnoreCase("mep")) {
				if (args.length == 0) {
					this.displayHelp(p);
					return true;
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("altar")) {
						p.sendMessage("");
						p.sendMessage(ChatColor.GOLD + " --- Info for: " + ChatColor.AQUA + "Altars" + ChatColor.GOLD + " ---");
						p.sendMessage(ChatColor.GRAY + "Tier 1 Altar (+1 level): " + ChatColor.GREEN + "http://imgur.com/a/Ux2XU");
						p.sendMessage(ChatColor.GRAY + "Tier 2 Altar (+2 levels): " + ChatColor.GREEN + "http://imgur.com/a/GTws9");
						return false;
					} else if (args[0].equalsIgnoreCase("list")) {
						p.sendMessage(ChatColor.GOLD + " --- MEP Enchantment List ---");
						
						for (CustomEnchantment en : this.enchants) {
							p.sendMessage(en.getName());
						}
						
						p.sendMessage(ChatColor.GOLD + "Use " + ChatColor.GRAY + "/" + ChatColor.GREEN + "mep" + ChatColor.DARK_GREEN + " <enchantment>" + ChatColor.GOLD + " for information about a particular enchantment");
						
						return true;
					} else if (args[0].equalsIgnoreCase("update") && sender.hasPermission("mep.update")) {
						if (!sender.isOp()) {
							return true;
						}
						if (!this.getConfig().getBoolean("canUpdate")) {
							sender.sendMessage(ChatColor.RED + "The config file for MoreEnchantsPlus is blocking this command, change: canUpdate to true to fix this.");
							return true;
						}
						sender.sendMessage(ChatColor.GOLD + "Updating MoreEnchantsPlus...");
						Updater updater = new Updater(this, 79543, this.getFile(), Updater.UpdateType.DEFAULT, true);
						if (updater.getResult().equals(Updater.UpdateResult.SUCCESS)) {
							sender.sendMessage(ChatColor.GREEN + "Successfully updated MoreEnchants+! Reload to enable the update!");
							return true;
						}
						if (updater.getResult().equals(Updater.UpdateResult.DISABLED)) {
							sender.sendMessage(ChatColor.RED + "Update disabled, Updater config is set to false");
							return true;
						}
						if (updater.getResult().equals(Updater.UpdateResult.NO_UPDATE)) {
							sender.sendMessage(ChatColor.GREEN + "Silly, MoreEnchants+ is already up to date!");
							return true;
						}
						if (updater.getResult().equals(Updater.UpdateResult.FAIL_NOVERSION)) {
							sender.sendMessage(ChatColor.RED + "The file version for MoreEnchants+ was setup incorrectly, please notify the plugin developer at: http://dev.bukkit.org/bukkit-plugins/moreenchatsplus/ . Chances are that this is currently being fixed.");
							return true;
						}
						sender.sendMessage(ChatColor.RED + "There was an error with the update, check the console for possible errors.");
						return true;
					} else {
						for (CustomEnchantment en : this.enchants) {
							if (!en.toString().equalsIgnoreCase(args[0])) {
								continue;
							}
							p.sendMessage("");
							p.sendMessage(ChatColor.GOLD + " --- Info for: " + en.getName() + ChatColor.GOLD + " ---");
							p.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.GREEN + en.userDescription());
							p.sendMessage(ChatColor.GRAY + "Usable Items: " + ChatColor.GREEN + en.userItems());
							p.sendMessage(ChatColor.GRAY + "Chance to Enchant: " + ChatColor.GREEN + en.getChance() + "%");
							p.sendMessage(ChatColor.GRAY + "Minimum EXP Cost: " + ChatColor.GREEN + en.getMinExp() + " Level(s)");
							p.sendMessage(ChatColor.GRAY + "Maximum Level: " + ChatColor.GREEN + en.getMaxLevel());
							return true;
						}
						this.displayHelp(p);
						return false;
					}
				}
				if (args.length > 1) {
					int level;
					if (!p.hasPermission("mep.command")) {
						return false;
					}
					ItemStack item = p.getInventory().getItemInMainHand();
					if (item.getType().equals(Material.AIR)) {
						p.sendMessage(ChatColor.RED + "[ME+] Please hold an item in your hand to enchant!");
						return false;
					}
					try {
						level = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						p.sendMessage(ChatColor.RED + "[ME+] Please enter a level to enchant at, not " + args[1] + "!");
						return false;
					}
					String enchantment = args[0];
					for (CustomEnchantment en : this.enchants) {
						if (!en.toString().equalsIgnoreCase(enchantment)) {
							continue;
						}
						en.forceEnchantItem(p.getInventory().getItemInMainHand(), level);
						p.sendMessage(ChatColor.GREEN + "[ME+] Successfully enchanted your " + item.getType().toString().toLowerCase().replaceAll("_", " ") + " with " + en.getName() + " " + RomanNumerals.getRoman(level));
						return true;
					}
					p.sendMessage(ChatColor.RED + "[ME+] Please enter a valid enchantment! '/mep list' for a list of enchantments");
					return false;
				}
			}
		}
		return true;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("mep")) {
			ArrayList<String> comps = new ArrayList<String>();
			if (args.length == 1) {
				if (!args[0].equals("")) {
					if (sender.hasPermission("mep.command")) {
						for (CustomEnchantment en : this.enchants) {
							if (!en.toString().toLowerCase().startsWith(args[0].toLowerCase())) {
								continue;
							}
							comps.add(en.toString());
						}
					}
					if ("help".startsWith(args[0].toLowerCase())) {
						comps.add("help");
					}
					if ("altar".startsWith(args[0].toLowerCase())) {
						comps.add("altar");
					}
					}
					if ("update".startsWith(args[0].toLowerCase()) && sender.hasPermission("mep.update")) {
						comps.add("update");
					}
				} else {
					for (CustomEnchantment en : this.enchants) {
						comps.add(en.toString());
					}
					comps.add("help");
					comps.add("altar");
					if (sender.hasPermission("mep.update")) {
						comps.add("update");
					}
				}
			}
			return comps;
		}
		return null;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!this.getConfig().getBoolean("canUpdate")) {
			return;
		}
		if (!e.getPlayer().hasPermission("mep.update")) {
			return;
		}
		boolean update = false;
		String webVersion = name.replaceAll("[^0-9.]", "");
		String currentVersion = this.getDescription().getVersion().replaceAll("[^0-9.]", "");
		int web = Integer.parseInt(name.replaceAll("[^0-9]", ""));
		int cur = Integer.parseInt(this.getDescription().getVersion().replaceAll("[^0-9]", ""));
		if (webVersion.length() > currentVersion.length()) {
			cur = (int) ((double) cur * Math.pow(10.0, webVersion.length() - currentVersion.length() - 1));
		}
		if (currentVersion.length() > webVersion.length()) {
			web = (int) ((double) web * Math.pow(10.0, currentVersion.length() - webVersion.length() - 1));
		}
		if (web > cur) {
			e.getPlayer().sendMessage(ChatColor.GREEN + " An update for MoreEnchants+ is available ");
			e.getPlayer().sendMessage(ChatColor.GREEN + " A " + ChatColor.GOLD + (type) + ChatColor.GREEN + " version " + ChatColor.GOLD + webVersion + ChatColor.GREEN + " for " + ChatColor.GOLD + version);
			e.getPlayer().sendMessage(ChatColor.GREEN + "/mep update to update");
		} else if (cur > web) {
			e.getPlayer().sendMessage(ChatColor.GREEN + " Ooo, you are using a version of MoreEnchants+ that hasn't been released yet! Enjoy :)");
		}
	}
}