package com.alfie51m.mCStatusChecker;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MCStatusChecker extends JavaPlugin implements Listener {

    private boolean verboseMode;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        loadConfigValues();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("MCStatusChecker enabled!");
    }

    // Load config values
    public void loadConfigValues() {
        FileConfiguration config = getConfig();
        verboseMode = config.getBoolean("verbose", false);
        getLogger().info("Verbose mode is " + (verboseMode ? "enabled" : "disabled"));
    }

    // Handle chat events
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player sender = event.getPlayer();
        FileConfiguration config = getConfig();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (message.contains(target.getName())) {

                for (String key : config.getKeys(false)) {
                    if (key.equalsIgnoreCase("verbose")) continue; // Skip verbose config

                    ConfigurationSection section = config.getConfigurationSection(key);
                    if (section == null) continue;

                    String placeholder = section.getString("placeholder", "");
                    String expectedResult = section.getString("expected_result", "");
                    String successMessage = ChatColor.translateAlternateColorCodes('&', section.getString("success_message", ""));
                    String failureMessage = ChatColor.translateAlternateColorCodes('&', section.getString("failure_message", ""));

                    String placeholderResult = PlaceholderAPI.setPlaceholders(target, placeholder);

                    // Verbose logging
                    if (verboseMode) {
                        getLogger().info("[" + key + "] Placeholder result for " + target.getName() + " using '" + placeholder + "': " + placeholderResult);
                    }

                    if (expectedResult.equalsIgnoreCase(placeholderResult)) {
                        sender.sendMessage(successMessage.replace("{player}", target.getName()));
                    } else if (!failureMessage.equalsIgnoreCase("disabled")) {
                        sender.sendMessage(failureMessage.replace("{player}", target.getName()));
                    }
                }
            }
        }
    }

    // Handle commands
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mcstatuschecker")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                loadConfigValues();
                sender.sendMessage(ChatColor.GREEN + "MCStatusChecker config reloaded!");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Usage: /mcstatuschecker reload");
            return true;
        }
        return false;
    }
}
