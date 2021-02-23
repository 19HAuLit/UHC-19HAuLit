package fr.gotta.uhc.commands;

import fr.gotta.uhc.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            World hub = Bukkit.getWorld("world");
            World arena = Bukkit.getWorld("arena");
            if (player.getWorld() == arena)
            {
                Location location = player.getLocation();
                ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 1);
                location.getWorld().dropItemNaturally(location, gapple);
            }
            else if (player.getWorld() != hub && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
            {
                main.playerLeft--;
                main.checkWin();
            }
            Location spawn = new Location(hub, main.hub_x, main.hub_y, main.hub_z);
            player.setGameMode(GameMode.SURVIVAL);
            main.clearEffect(player);
            player.teleport(spawn);
            player.setExp(0);
            player.setLevel(0);
            main.clearStuff(player);
            player.sendMessage(main.prefix+"Bienvenue au spawn !");
        }

        return false;
    }
}
