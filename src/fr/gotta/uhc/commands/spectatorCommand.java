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

public class spectatorCommand implements CommandExecutor
{
    private Main main;

    public spectatorCommand(Main main)
    {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            World uhc = Bukkit.getWorld("uhc");
            if (main.state == "wait") player.sendMessage(main.prefix+"Vous ne pouvez pas spec une game qui n'a pas commencé");
            else if (player.getWorld() == uhc && player.getGameMode() == GameMode.SURVIVAL) player.sendMessage(main.prefix+"Vous ne pouvez pas spec une game à la quelle vous participez");
            else
            {
                Location location = new Location(uhc, 0, 100, 0);
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(location);
                player.sendMessage(main.prefix+"Vous êtes entrain de spec la game !");
            }
        }
        else
        {
            commandSender.sendMessage(main.prefix+"La console ne peut pas faire cette commande");
        }
        return false;
    }
}
