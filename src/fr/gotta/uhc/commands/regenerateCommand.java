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
        World hub = Bukkit.getWorld("world");
        Location spawn = new Location(hub, main.hub_x, main.hub_y, main.hub_z);
        World uhc = Bukkit.getWorld("uhc");
        main.unLoadWord(uhc, spawn);
        File uhcFile = new File(System.getProperty("user.dir") + "\\uhc");
        main.fileDelete(uhcFile);
        WorldCreator uhcWorld = new WorldCreator("uhc");
        uhcWorld.environment(World.Environment.NORMAL);
        uhcWorld.createWorld();
        commandSender.sendMessage(main.prefix+"La map uhc a bien été regen !");
        Bukkit.reload();
        return false;
    }


}
