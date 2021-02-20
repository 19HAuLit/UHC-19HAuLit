package fr.gotta.uhc.commands;

import fr.gotta.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class stateCommand implements CommandExecutor
{
    private Main main;

    public stateCommand(Main main)
    {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        World uhc = Bukkit.getWorld("uhc");
        double border_size = uhc.getWorldBorder().getSize();
        commandSender.sendMessage(main.prefix+"Etat de la game: "+main.state+", temps de la game: "+main.uhcTime+", taille de la map: "+border_size+", Joueurs restants: "+main.playerLeft);
        return false;
    }
}
