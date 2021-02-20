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

public class previewCommand implements CommandExecutor
{
    private Main main;

    public previewCommand(Main main)
    {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (commandSender instanceof Player && main.state == "wait")
        {
            Player player = (Player) commandSender;
            World uhc = Bukkit.getWorld("uhc");
            if (player.getWorld() == uhc)
            {
                player.setGameMode(GameMode.SURVIVAL);
                World hub = Bukkit.getWorld("world");
                Location location = new Location(hub, main.hub_x, main.hub_y, main.hub_z);
                player.teleport(location);
                player.sendMessage(main.prefix+"Bienvenue au spawn !");
            }
            else
                {
                    player.setGameMode(GameMode.SPECTATOR);
                    Location location = new Location(uhc, 0.5, 200, 0.5);
                    player.teleport(location);
                    player.sendMessage(main.prefix+"Vous êtes bien sur la map de l'uhc");
                }
        }
        else
            {
                commandSender.sendMessage(main.prefix+"Il est impossible de preview la map quand une game est en cour");
            }
        return false;
    }
}
