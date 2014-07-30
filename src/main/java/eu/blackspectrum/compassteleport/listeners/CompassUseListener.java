package eu.blackspectrum.compassteleport.listeners;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;

import eu.blackspectrum.compassteleport.*;
import eu.blackspectrum.compassteleport.tasks.*;

public class CompassUseListener implements Listener {
	CompassTeleport plugin;
	
	public CompassUseListener(CompassTeleport instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		World world, targetWorld;
		double CotWx, CotWz, CotWradius, x, y, z;
		Location playerLocation;
		String targetWorldName;
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			if (event.hasItem() && event.getItem().getTypeId() == 345) {
				world = event.getPlayer().getWorld();
				if(plugin.teleportingPlayers.containsKey(event.getPlayer().getName()))
					plugin.teleportingPlayers.put(event.getPlayer().getName(), false);
				else if(plugin.yamlHandler.config.contains(world.getName())){
					CotWx = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWx", 0);
					CotWz = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWz", 0);
					CotWradius = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWradius", 10);
					playerLocation = event.getPlayer().getLocation();
					if(playerLocation.distance(new Location(world, CotWx, playerLocation.getY(), CotWz)) <= CotWradius){
						targetWorldName = plugin.yamlHandler.config.getString(world.getName() + ".targetWorld", world.getName());
						if(plugin.getServer().getWorld(targetWorldName) != null)
							targetWorld = plugin.getServer().getWorld(targetWorldName);
						else
							targetWorld = world;
						x = plugin.yamlHandler.config.getDouble(world.getName() + ".targetx", 0);
						z = plugin.yamlHandler.config.getDouble(world.getName() + ".targetz", 0);
						y = plugin.yamlHandler.config.getDouble(world.getName() + ".targety", targetWorld.getHighestBlockYAt(new Location(targetWorld, x, 0, z)));
						plugin.teleportingPlayers.put(event.getPlayer().getName(), true);
						TeleportTask task = new TeleportTask(this.plugin, event.getPlayer(), new Location(targetWorld, x, y, z));
						task.runTaskTimer(plugin, 0, 20);
					}
					else
						event.getPlayer().sendMessage(plugin.yamlHandler.config.getString("config.cantUseMessage"));
				}
			}
	}
}
