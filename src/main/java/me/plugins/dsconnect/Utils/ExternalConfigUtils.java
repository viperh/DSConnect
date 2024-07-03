package me.plugins.dsconnect.Utils;

import me.plugins.dsconnect.DSConnect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("all")
public class ExternalConfigUtils {
    private final String name;
    private final File file;
    private FileConfiguration config;

    public ExternalConfigUtils(String name){
        this.name = name;
        this.file = new File(DSConnect.instance.getDataFolder().getPath(), name);
        this.config = loadConfiguration();
    }

    private FileConfiguration loadConfiguration(){
        if (!file.exists()) {
            try{
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            catch(Exception e){
                throw new Error(e);
            }
            DSConnect.instance.getLogger().severe("File " + this.file.getName() + " not found, creating a new one...");

            DSConnect.instance.getLogger().info("\nPath: " + this.file.getPath() + "\n");
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfiguration(){
        return config;
    }
    public void reload(){
        this.config = YamlConfiguration.loadConfiguration(file);
    }
    public void saveConfigurationFile(){
        try {
            this.config.save(file);
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public static ExternalConfigUtils of(String name){
        return new ExternalConfigUtils(name);
    }
}
