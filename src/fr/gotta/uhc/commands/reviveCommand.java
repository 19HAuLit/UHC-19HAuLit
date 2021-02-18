package fr.gotta.uhc.commands;

import fr.gotta.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class reviveCommand implements CommandExecutor
{
    private Main main;

    public reviveCommand(Main main)
    {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (main.state == "wait")
        {
            commandSender.sendMessage(main.prefix+"Il est impossible de revive le joueur");
        }
        else if (strings.length >= 1)
        {
            commandSender.sendMessage(main.prefix+"En dev...");
        }
        else if (commandSender instanceof Player)
        {
            Player dead = (Player) commandSender;
            World hub = Bukkit.getWorld("world");
            if (dead.getGameMode() != GameMode.SURVIVAL || dead.getWorld() == hub)
            {
                World uhc = Bukkit.getWorld("uhc");
                int x = dead.getLocation().getBlockX();
                int y = dead.getLocation().getBlockY();
                int z = dead.getLocation().getBlockZ();
                Location location = new Location(uhc, x, y, z);
                dead.setGameMode(GameMode.SURVIVAL);
                dead.getInventory().clear();
                dead.setFoodLevel(20);
                dead.setSaturation(20);
                dead.setHealth(20);
                dead.getActivePotionEffects().clear();
                dead.teleport(location);
                Bukkit.broadcastMessage(main.prefix + dead.getDisplayName() + " viens d'être revive !");
                main.playerLeft++;
            }
            else
                {
                    dead.sendMessage(main.prefix+"Vous ne pouvez pas vous revive si vous êtes déjà en vie !");
                }
        }
        else
        {
            commandSender.sendMessage(main.prefix+"Il est impossible de revive le joueur");
        }
        return false;
    }
}
