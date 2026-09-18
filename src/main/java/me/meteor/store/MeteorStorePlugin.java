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

public class MeteorStorePlugin extends JavaPlugin implements CommandExecutor {

    // Store active codes: Code -> PurchaseData
    private final Map<String, PurchaseData> pendingPurchases = new HashMap<>();

    @Override
    public void onEnable() {
        if (this.getCommand("redeem") != null) {
            this.getCommand("redeem").setExecutor(this);
        }
        getLogger().info("MeteorStorePlugin enabled successfully!");
    }

    // Register web codes dynamically
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
        pendingPurchases.remove(inputCode);

        grantReward(player, data.rewardType);
        player.sendMessage(ChatColor.GREEN + "Code redeemed! Run /pay server " + (int) data.price + "m to finish payment.");

        return true;
    }

    private void grantReward(Player player, String rewardType) {
        String name = player.getName();
        switch (rewardType.toLowerCase()) {
            // Ranks
            case "meteor":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent set meteor");
                break;
            case "meteorplus":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent set meteor+");
                break;
            case "meteorplusplus":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent set meteor++");
                break;
            case "inferno":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent set inferno");
                break;
            case "infernoplus":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent set inferno+");
                break;
            case "infernoplusplus":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent set inferno++");
                break;

            // Keys
            case "common":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give " + name + " common 15");
                break;
            case "gold":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give " + name + " gold 35");
                break;
            case "prime":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give " + name + " prime 5");
                break;

            // Shards
            case "shards100":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shards give " + name + " 100");
                break;
            case "shards400":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shards give " + name + " 400");
                break;
            case "shards1500":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shards give " + name + " 1500");
                break;
            case "shards3000":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shards give " + name + " 3000");
                break;
            case "shards8000":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shards give " + name + " 8000");
                break;
            case "shards10000":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shards give " + name + " 10000");
                break;

            default:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say Granted " + rewardType + " to " + name);
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
