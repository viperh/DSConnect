package me.plugins.dsconnect.Socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import me.plugins.dsconnect.DSConnect;
import me.plugins.dsconnect.Listeners.SocketListener;
import me.plugins.dsconnect.Utils.ExternalConfigUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketManager {

    private boolean continueFlag;

    private String url;

    private final ExternalConfigUtils config;

    private Socket socket;

    public SocketManager(ExternalConfigUtils configUtils){

        this.config = configUtils;

        String host = System.getenv("SOCKET_HOST");
        int port;
        try{
            port = Integer.parseInt(System.getenv("SOCKET_PORT"));
        }
        catch(NumberFormatException e){
            port = -1;
        }

        if (host == null || host.isEmpty() || port == -1){
            DSConnect.instance.getLogger().severe("Invalid host or port!");
        }

        //this.url = "http://" + host + ":" + port;
        this.url = "https://bot.qviperhserver.software";
        DSConnect.instance.getLogger().info("Connecting URL: " + this.url);

    }

    public void initializeConnection(){
        this.continueFlag = false;
        while(true){
            try{
                DSConnect.instance.getLogger().info("Getting channelId from config.yml...");
                String channelId = config.getConfiguration().getString("channelId");
                DSConnect.instance.getLogger().info("ChannelID: " + channelId);

                DSConnect.instance.getLogger().info("Creating Options for socket...");

                Map<String, List<String>> headers = new HashMap<>();

                headers.put("channelId", Collections.singletonList(channelId));

                IO.Options options = IO.Options.builder()
                        .setExtraHeaders(headers)
                        .build();

                DSConnect.instance.getLogger().info("Creating socket connection....");

                this.socket = IO.socket(this.url, options);

                DSConnect.instance.getLogger().info("Adding listeners...");

                this.socket.on("message", new SocketListener());

                this.socket.on(Socket.EVENT_CONNECT, (args) -> {
                    DSConnect.instance.getLogger().info("Connected to socket server!");
                    this.continueFlag = true;
                });

                if(this.continueFlag){
                    break;
                }
            }
            catch(Exception e){
                DSConnect.instance.getLogger().severe("Failed to connect to discord bot. Retrying in 10 seconds...");
            }

            try{
                DSConnect.instance.getLogger().info("Retrying to connect to socket server!...");
                Thread.sleep(10000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }





    public void sendMessage(String message){
        this.socket.emit("message", message);
    }


    public void connect(){
        this.initializeConnection();
    }

    public void disconnect(){
        this.socket.disconnect();
        this.socket = null;
    }

    public void reconnect(){
        this.disconnect();
        this.initializeConnection();
    }


}
