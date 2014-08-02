package eu.blackspectrum.compassteleport.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import eu.blackspectrum.compassteleport.*;
import eu.blackspectrum.compassteleport.tasks.TeleportTask;

public class PlayerListener implements Listener {
	CompassTeleport plugin;
	
	public PlayerListener(CompassTeleport instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldChange(PlayerChangedWorldEvent event) {
		World world = event.getPlayer().getWorld();
		double x, z;
		if(plugin.yamlHandler.config.contains(world.getName())){
			x = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWx", 0);
			z = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWz", 0);
			event.getPlayer().setCompassTarget(new Location(world, x, 0, z));
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		World world = event.getPlayer().getWorld();
		double x, z;
		if(plugin.yamlHandler.config.contains(world.getName())){
			x = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWx", 0);
			z = plugin.yamlHandler.config.getDouble(world.getName() + ".CotWz", 0);
			event.getPlayer().setCompassTarget(new Location(world, x, 0, z));
		}
	}
	
	@EventHandler
	public void onPlayerHurt(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player)
			if(plugin.teleportingPlayers.containsKey(((Player)event.getEntity()).getName())) {
				plugin.teleportingPlayers.put(((Player)event.getEntity()).getName(), false);
				if(plugin.teleportingPlayers.get(((Player)event.getEntity()).getName()))
					((Player)event.getEntity()).sendMessage(plugin.yamlHandler.config.getString("config.failMessage"));
			}
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
				if(plugin.teleportingPlayers.get(event.getPlayer().getName()))
					event.getPlayer().sendMessage(plugin.yamlHandler.config.getString("config.failMessage"));
				plugin.teleportingPlayers.put(event.getPlayer().getName(), false);
			}
		}
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
				{
					if(plugin.teleportingPlayers.get(event.getPlayer().getName()))
						event.getPlayer().sendMessage(plugin.yamlHandler.config.getString("config.failMessage"));
					plugin.teleportingPlayers.put(event.getPlayer().getName(), false);
				}
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
