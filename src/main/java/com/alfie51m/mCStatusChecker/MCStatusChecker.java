package com.alfie51m.mCStatusChecker;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MCStatusChecker extends JavaPlugin implements Listener {

    private String placeholder;
    private String expectedResult;
    private String successMessage;
    private String failureMessage;

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

    public void loadConfigValues() {
        FileConfiguration config = getConfig();
        placeholder = config.getString("placeholder", "Not set");
        expectedResult = config.getString("expected_result", "Not set");
        successMessage = ChatColor.translateAlternateColorCodes('&', config.getString("success_message", "Not set"));
        failureMessage = ChatColor.translateAlternateColorCodes('&', config.getString("failure_message", "Not set"));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player sender = event.getPlayer();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (message.contains(target.getName())) {
                String placeholderResult = PlaceholderAPI.setPlaceholders(target, placeholder);
                getLogger().info("Placeholder result for " + target.getName() + ": " + placeholderResult);

                if (expectedResult.equalsIgnoreCase(placeholderResult)) {
                    sender.sendMessage(successMessage.replace("{player}", target.getName()));
                } else if (!failureMessage.equalsIgnoreCase("disabled")) {
                    sender.sendMessage(failureMessage.replace("{player}", target.getName()));
                }
            }
        }
    }
}
