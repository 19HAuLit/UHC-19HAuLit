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

public class reviveCommand implements CommandExecutor
{
    private Main main;

    public reviveCommand(Main main)
    {
        this.main = main;
    }

    private void revive(World world, Player player)
    {
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        Location location = new Location(world, x, y, z);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setHealth(20);
        player.getActivePotionEffects().clear();
        player.teleport(location);
        Bukkit.broadcastMessage(main.prefix + player.getDisplayName() + " viens d'être revive !");
        main.playerLeft++;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        World hub = Bukkit.getWorld("world");
        World uhc = Bukkit.getWorld("uhc");
        if (main.state == "wait")
        {
            commandSender.sendMessage(main.prefix+"Il est impossible de revive le joueur");
        }
        else if (strings.length >= 1)
        {
            Boolean isRevive = false;
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (player.getGameMode() != GameMode.SURVIVAL || player.getWorld() == hub)
                {
                    if (player.getDisplayName().equalsIgnoreCase(strings[0]))
                    {
                        revive(uhc, player);
                        isRevive = true;
                    }
                }
            }
            if (!isRevive) commandSender.sendMessage(main.prefix+"Il est impossible de revive le joueur");
        }
        else if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            if (player.getGameMode() != GameMode.SURVIVAL || player.getWorld() == hub) revive(uhc, player);
            else player.sendMessage(main.prefix+"Vous ne pouvez pas vous revive si vous êtes déjà en vie !");
        }
        else commandSender.sendMessage(main.prefix+"Il est impossible de revive le joueur");
        return false;
    }
}
