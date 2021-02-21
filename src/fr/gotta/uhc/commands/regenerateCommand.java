package fr.gotta.uhc.commands;
import fr.gotta.uhc.Main;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class regenerateCommand implements CommandExecutor
{
    private Main main;

    public regenerateCommand(Main main)
    {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        // Tp world
        World hub = Bukkit.getWorld("world");
        Location spawn = new Location(hub, main.hub_x, main.hub_y, main.hub_z);
        // Regen uhc
        World uhc = Bukkit.getWorld("uhc");
        main.unLoadWord(uhc, spawn);
        File uhcFile = new File(System.getProperty("user.dir") + "\\uhc");
        main.fileDelete(uhcFile);
        WorldCreator uhcWorld = new WorldCreator("uhc");
        uhcWorld.environment(World.Environment.NORMAL);
        uhcWorld.createWorld();
        // Regen uhc_nether
        World uhc_nether = Bukkit.getWorld("uhc_nether");
        main.unLoadWord(uhc_nether, spawn);
        File uhc_netherFile = new File(System.getProperty("user.dir") + "\\uhc_nether");
        main.fileDelete(uhc_netherFile);
        WorldCreator uhc_netherWorld = new WorldCreator("uhc_nether");
        uhc_netherWorld.environment(World.Environment.NETHER);
        uhc_netherWorld.createWorld();
        // Finish
        commandSender.sendMessage(main.prefix+"La map uhc a bien été regen !");
        Bukkit.reload();
        return false;
    }


}
