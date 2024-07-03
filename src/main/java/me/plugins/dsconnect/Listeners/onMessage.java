package me.plugins.dsconnect.Listeners;

import me.plugins.dsconnect.Socket.SocketManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onMessage implements Listener {
    private final SocketManager socketManager;

    public onMessage(SocketManager socketManager){
        this.socketManager = socketManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String message = event.getMessage();

        String player = event.getPlayer().getName();

        String finalMessage = player + ": " + message;

        socketManager.sendMessage(finalMessage);

    }

}
