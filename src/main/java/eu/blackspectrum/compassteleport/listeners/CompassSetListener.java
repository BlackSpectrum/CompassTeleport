package eu.blackspectrum.compassteleport.listeners;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import eu.blackspectrum.compassteleport.*;

public class CompassSetListener implements Listener {
	CompassTeleport plugin;
	
	public CompassSetListener(CompassTeleport instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldChange(PlayerTeleportEvent event) {
		if(event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
		World world = event.getPlayer().getWorld();
		double x, z;
		if(plugin.yamlHandler.config.contains(world.getName())){
			x = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWx", 0);
			z = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWz", 0);
			event.getPlayer().setCompassTarget(new Location(world, x, 0, z));
		}
	}
}
