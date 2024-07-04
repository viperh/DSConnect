package me.plugins.dsconnect.Socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import me.plugins.dsconnect.DSConnect;
import me.plugins.dsconnect.Listeners.SocketListener;
import me.plugins.dsconnect.Utils.ExternalConfigUtils;

import java.util.Collections;

public class SocketManager {

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
        while(true){
            try{

                String channelId = config.getConfiguration().getString("channelId");

                if(channelId == null){
                    throw new Error("Channel ID not found in config.yml");
                }
                IO.Options options = IO.Options.builder()
                        .setExtraHeaders(Collections.singletonMap("channelId", Collections.singletonList(channelId)))
                        .build();

                this.socket = IO.socket(this.url, options);

                this.addListener("message", new SocketListener());
                this.addListener("connect", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        DSConnect.instance.getLogger().info("Connected to socket server!");
                    }
                });

                if (this.socket.isActive()){
                    DSConnect.instance.getLogger().info("Connected to discord bot! Channel ID: " + channelId);
                }


                return;
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



    public void addListener(String event, SocketListener listener){
        this.socket.on(event, listener);
    }

    public void addListener(String event, Emitter.Listener listener){
        this.socket.on(event, listener);
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
