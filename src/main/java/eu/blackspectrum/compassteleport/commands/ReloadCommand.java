package eu.blackspectrum.compassteleport.commands;

import eu.blackspectrum.compassteleport.*;
import org.bukkit.command.*;
import java.util.*;

public class ReloadCommand extends AbstractCommand
{
    public ReloadCommand(final CompassTeleport instance) {
        super(instance, "compassteleportreload");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final List<String> args) {
        if (args.size() == 0) {
            this.plugin.yamlHandler.loadConfig();
            sender.sendMessage("Compass Teleport config file reloaded.");
            return true;
        }
        return false;
    }
    
}
