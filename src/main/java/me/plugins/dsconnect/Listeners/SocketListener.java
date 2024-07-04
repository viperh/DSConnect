package me.plugins.dsconnect.Listeners;


import io.socket.emitter.Emitter;
import me.plugins.dsconnect.DSConnect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SocketListener implements Emitter.Listener {


    @Override
    public void call(Object... objects) {

        //for (Player player : Bukkit.getOnlinePlayers()){
        //    player.sendMessage(objects[0].toString());
        //}

        DSConnect.instance.getLogger().info(objects[0].toString());

    }
}
