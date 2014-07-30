package eu.blackspectrum.compassteleport.handlers;

import eu.blackspectrum.compassteleport.*;
import eu.blackspectrum.compassteleport.commands.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class CommandHandler implements CommandExecutor
{
    final CompassTeleport plugin;
    HashMap<String, AbstractCommand> commands;
    
    public CommandHandler(final CompassTeleport instance) {
        super();
        this.commands = new HashMap<String, AbstractCommand>();
        this.plugin = instance;
        this.plugin.getCommand("compassteleportreload").setExecutor((CommandExecutor)this);
        this.registerCommands(new AbstractCommand[] { new ReloadCommand(this.plugin) });
    }
    
    private void registerCommands(final AbstractCommand[] abstractCommands) {
        for (final AbstractCommand abstractCommand : abstractCommands) {
            this.commands.put(abstractCommand.getName(), abstractCommand);
            final List<String> aliases = (List<String>)abstractCommand.getAliases();
            if (abstractCommand.getAliases() != null) {
                for (final String alias : aliases) {
                    this.commands.put(alias, abstractCommand);
                }
            }
        }
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final AbstractCommand abstractCommand = this.commands.get(label);
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used in game!");
            return true;
        }
        if (abstractCommand.getPermission() != null && !sender.hasPermission(abstractCommand.getPermission())) {
            sender.sendMessage("You don't have the permission to use this command!");
            return true;
        }
        if (abstractCommand.onCommand(sender, Arrays.asList(args).subList(0, args.length)) == false && abstractCommand.getUsage() != null){
			sender.sendMessage(abstractCommand.getUsage());
		}
        return true;
    }
}
