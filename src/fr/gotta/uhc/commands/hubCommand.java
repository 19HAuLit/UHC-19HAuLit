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

public class hubCommand implements CommandExecutor
{
    private Main main;

    public hubCommand(Main main)
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
            if (player.getWorld() == uhc && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
            {
                main.playerLeft--;
                main.checkWin();
            }
            World hub = Bukkit.getWorld("world");
            Location spawn = new Location(hub, main.hub_x, main.hub_y, main.hub_z);
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(spawn);
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.sendMessage(main.prefix+"Bienvenue au spawn !");
        }

        return false;
    }
}
