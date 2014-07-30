package eu.blackspectrum.compassteleport.tasks;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import eu.blackspectrum.compassteleport.*;
 
public class TeleportTask extends BukkitRunnable {
 
    private final CompassTeleport plugin;
    private final Location location;
    private final Player player;
    private int timer;
 
    public TeleportTask(CompassTeleport plugin, Player player, Location location) {
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.timer = plugin.yamlHandler.config.getInt("config.timerLength", 5);
    }

    public void run() {
    	if(!plugin.teleportingPlayers.get(player.getName())) {
    		plugin.teleportingPlayers.remove(player.getName());
    		this.cancel();
    	}
    	else if(timer > 0)
        	player.sendMessage("Teleporting in " + timer-- + " seconds. Use again to cancel.");
    	else {
    		player.teleport(location);
    		player.sendMessage(plugin.yamlHandler.config.getString("config.successMessage"));
    		plugin.teleportingPlayers.remove(player.getName());
    		this.cancel();
    	}
    }
 
}
