package com.meteorsmp.store;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MeteorStorePlugin extends JavaPlugin implements CommandExecutor {

    // Store active codes: Code -> PurchaseInfo
    private final Map<String, PurchaseData> pendingPurchases = new HashMap<>();

    @Override
    public void onEnable() {
        this.getCommand("redeem").setExecutor(this);
        getLogger().info("MeteorStorePlugin enabled successfully!");
    }

    // Method to register a newly generated web code dynamically
    public void registerCode(String code, String rewardType, double price) {
        pendingPurchases.put(code.toUpperCase(), new PurchaseData(rewardType, price));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /redeem <code>");
            return true;
        }

        String inputCode = args[0].toUpperCase();

        if (!pendingPurchases.containsKey(inputCode)) {
            player.sendMessage(ChatColor.RED + "Invalid or already redeemed code!");
            return true;
        }

        PurchaseData data = pendingPurchases.get(inputCode);

        // Consume code immediately to prevent spam/duplication
        pendingPurchases.remove(inputCode);

        // Execute reward logic
        grantReward(player, data.rewardType);
        player.sendMessage(ChatColor.GREEN + "Code redeemed! Run /pay server " + (int)data.price + "m to finish payment.");

        return true;
    }

    private void grantReward(Player player, String rewardType) {
        switch (rewardType.toLowerCase()) {
            case "inferno":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set inferno");
                break;
            case "shards100":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shards give " + player.getName() + " 100");
                break;
            default:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say Granted " + rewardType + " to " + player.getName());
                break;
        }
    }

    private static class PurchaseData {
        String rewardType;
        double price;

        PurchaseData(String rewardType, double price) {
            this.rewardType = rewardType;
            this.price = price;
        }
    }
}
