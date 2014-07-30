package eu.blackspectrum.compassteleport.handlers;

import eu.blackspectrum.compassteleport.*;
import org.bukkit.configuration.file.*;
import java.io.*;

public class YamlHandler
{
    CompassTeleport plugin;
    File configFile;
    public FileConfiguration config;
    
    public YamlHandler(final CompassTeleport instance) {
        super();
        this.plugin = instance;
        this.setupYamls();
        this.loadConfig();
    }
    
    public void setupYamls() {
        this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (!this.configFile.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
    }
    
    public void loadConfig() {
    	this.config = new YamlConfiguration();
        try {
            this.config.load(this.configFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
