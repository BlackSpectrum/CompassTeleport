package eu.blackspectrum.compassteleport.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

import eu.blackspectrum.compassteleport.*;

public class PlayerHurtListener implements Listener {
	CompassTeleport plugin;
	
	public PlayerHurtListener(CompassTeleport instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerHurt(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player)
			if(plugin.teleportingPlayers.containsKey(((Player)event.getEntity()).getName())) {
				plugin.teleportingPlayers.put(((Player)event.getEntity()).getName(), false);
	    		((Player)event.getEntity()).sendMessage(plugin.yamlHandler.config.getString("config.failMessage"));
			}
	}
}
