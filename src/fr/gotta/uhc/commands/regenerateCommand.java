package fr.gotta.uhc.commands;
import fr.gotta.uhc.Main;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;

public class regenerateCommand implements CommandExecutor
{
    private Main main;

    public regenerateCommand(Main main)
    {
        this.main = main;
    }

    public void loadMap(World world)
    {
        int map_size = 100;
        if (main.getConfigBool("uhc.auto_border")) map_size = main.getConfigInt("uhc.auto_border_limit");
        else map_size = main.getConfigInt("uhc.map_size");
        for (int x = map_size/2; x >= -map_size/2; x--)
        {
            for (int z = map_size/2; z >= -map_size/2; z--)
            {
                if (x%16 == 0 && z%16 == 0)
                {
                    world.regenerateChunk(x/16, z/16);
                }
                if (x%160 == 0 && z == 0)
                {
                    Bukkit.broadcastMessage(main.prefix+"Chunk ("+map_size/2/16+", "+map_size/2/16+") à ("+x/16+", "+z/16+") ont été générés");
                }
            }
        }
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
        if (strings.length >= 2)
        {
            long seed = Long.parseLong(strings[1]);
            uhcWorld.seed(seed);
        }
        uhcWorld.createWorld();
        uhc = Bukkit.getWorld("uhc");
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

        return false;
    }


}
