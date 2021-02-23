package fr.gotta.uhc.commands;

import fr.gotta.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class invseeCommand implements CommandExecutor {

    private Main main;

    public invseeCommand(Main main)
    {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (commandSender instanceof Player)
        {
            Player see = (Player) commandSender;
            Boolean isOpen = false;
            if (strings.length >= 1)
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (player.getDisplayName().equalsIgnoreCase(strings[0]))
                    {
                        if (see.isOp() || see.getGameMode() == GameMode.SPECTATOR)
                        {
                            Inventory inv = player.getInventory();
                            see.openInventory(inv);
                            isOpen = true;
                        }
                    }
                }
                if (!isOpen) see.sendMessage(main.prefix+"Vous ne pouvez pas ouvrir cette inventaire !");
            }
        }
        else
        {
            commandSender.sendMessage(main.prefix+"Vous ne pouvez pas faire cette commande !");
        }
        return false;
    }
}
