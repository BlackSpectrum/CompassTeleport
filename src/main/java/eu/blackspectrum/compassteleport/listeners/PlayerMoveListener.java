package eu.blackspectrum.compassteleport.listeners;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import eu.blackspectrum.compassteleport.*;

public class PlayerMoveListener implements Listener {
	CompassTeleport plugin;
	
	public PlayerMoveListener(CompassTeleport instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		double CotWx, CotWz, CotWradius;
		Location playerLocation;
		World world;
		if(plugin.teleportingPlayers.containsKey(event.getPlayer().getName())) {
			world = event.getPlayer().getWorld();
			CotWx = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWx", 0);
			CotWz = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWz", 0);
			CotWradius = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWradius", 10);
			playerLocation = event.getPlayer().getLocation();
			if(playerLocation.distance(new Location(world, CotWx, playerLocation.getY(), CotWz)) > CotWradius) {
				plugin.teleportingPlayers.put(event.getPlayer().getName(), false);
	    		event.getPlayer().sendMessage(plugin.yamlHandler.config.getString("config.failMessage"));
			}
		}
	}
}
