package fr.gotta.uhc.commands;

import fr.gotta.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class vanishCommand implements CommandExecutor
{

    private Main main;

    public vanishCommand(Main main)
    {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            for (Player p : Bukkit.getOnlinePlayers())
            {
                p.hidePlayer(player);
                p.sendMessage(main.prefix+player.getDisplayName()+" nous a quitté(e) lâchement...");
            }
            player.sendMessage(main.prefix+"Vous êtes maintenant en Vanish, pour le désactivé déco/reco");
        }
        else
        {
            commandSender.sendMessage(main.prefix+"Vous ne pouvez pas faire cette commande");
        }
        return false;
    }
}
