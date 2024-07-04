package me.plugins.dsconnect;

import me.plugins.dsconnect.Listeners.onMessage;
import me.plugins.dsconnect.Socket.SocketManager;
import me.plugins.dsconnect.Utils.ExternalConfigUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class DSConnect extends JavaPlugin {

    public static DSConnect instance;


    ExternalConfigUtils configUtils;

    SocketManager socketManager;

    @Override
    public void onEnable() {

        instance = this;

        this.configUtils = new ExternalConfigUtils("config.yml");

        this.socketManager = new SocketManager(configUtils);

        getServer().getPluginManager().registerEvents(new onMessage(socketManager), this);

        this.getLogger().info("Plugin has been enabled!");

        this.socketManager.connect();
    }

    @Override
    public void onDisable() {
        this.socketManager.disconnect();
    }
}
