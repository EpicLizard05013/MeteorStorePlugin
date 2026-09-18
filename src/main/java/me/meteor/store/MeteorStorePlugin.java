package me.meteor.store;

import org.bukkit.plugin.java.JavaPlugin;

public final class MeteorStorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MeteorStorePlugin has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MeteorStorePlugin has been disabled.");
    }
}
