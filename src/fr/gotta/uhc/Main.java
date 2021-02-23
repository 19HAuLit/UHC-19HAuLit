package fr.gotta.uhc;

import fr.gotta.uhc.commands.*;
import fr.gotta.uhc.listeners.listeners;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.nio.file.Files;
import java.util.Collection;

public class Main extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        // DEFAULT GAMERULE
        Bukkit.setDefaultGameMode(GameMode.SURVIVAL);
        World hub = Bukkit.getWorld("world");
        if (hub.getPVP()) hub.setPVP(false);
        if (hub.getAnimalSpawnLimit() != 0) hub.setAnimalSpawnLimit(0);
        if (hub.getMonsterSpawnLimit() != 0) hub.setMonsterSpawnLimit(0);
        hub.setGameRuleValue("doFireTick", "false");
        // Load World
        WorldCreator uhcWorld = new WorldCreator("uhc");
        uhcWorld.environment(World.Environment.NORMAL);
        uhcWorld.createWorld();
        WorldCreator uhc_netherWorld = new WorldCreator("uhc_nether");
        uhc_netherWorld.environment(World.Environment.NETHER);
        uhc_netherWorld.createWorld();
        WorldCreator arena = new WorldCreator("arena");
        arena.environment(World.Environment.NORMAL);
        arena.createWorld();
        Bukkit.getWorld("arena").getWorldBorder().setSize(200);
        Bukkit.getWorld("arena").setGameRuleValue("naturalRegeneration", "false");
        Bukkit.getWorld("arena").setGameRuleValue("keepInventory", "true");
        if (Bukkit.getWorld("arena").getAnimalSpawnLimit() != 0) Bukkit.getWorld("arena").setAnimalSpawnLimit(0);
        if (Bukkit.getWorld("arena").getMonsterSpawnLimit() != 0) Bukkit.getWorld("arena").setMonsterSpawnLimit(0);
        // LISTENERS
        getServer().getPluginManager().registerEvents(new listeners(this), this);
        // COMMANDS
        getCommand("hub").setExecutor(new hubCommand(this));
        getCommand("alert").setExecutor(new alertCommand(this));
        getCommand("start").setExecutor(new startCommand(this));
        getCommand("preview").setExecutor(new previewCommand(this));
        getCommand("regenerate").setExecutor(new regenerateCommand(this));
        getCommand("state").setExecutor(new stateCommand(this));
        getCommand("spectator").setExecutor(new spectatorCommand(this));
        getCommand("revive").setExecutor(new reviveCommand(this));
        getCommand("invsee").setExecutor(new invseeCommand(this));
        getCommand("vanish").setExecutor(new vanishCommand(this));
        getCommand("arena").setExecutor(new arenaCommand(this));
        // ScoreBoard
        for(Player player : Bukkit.getOnlinePlayers())
        {
            scoreBoard(player);
        }
        return;
    }

    @Override
    public void onDisable()
    {
        World uhc = Bukkit.getWorld("uhc");
        World uhc_nether = Bukkit.getWorld("uhc_nether");
        World arena = Bukkit.getWorld("arena");
        World world = Bukkit.getWorld("world");
        Location spawn = new Location(world, 0, 64, 0);
        unLoadWord(uhc, spawn);
        unLoadWord(uhc_nether, spawn);
        unLoadWord(arena, spawn);
        return;
    }
    public String getConfigString(String path)
    {
        String str = getConfig().getString(path);
        String msg = str.replace("&","§");
        return msg;
    }

    public int uhcTime = 0;
    public int playerLeft = 0;
    public double hub_x = getConfigDouble("world.hub.x");
    public double hub_y = getConfigDouble("world.hub.y");
    public double hub_z = getConfigDouble("world.hub.z");
    public String prefix = getConfigString("message.prefix");
    public String state = "wait";
    public String bukkit_version = Bukkit.getBukkitVersion();

    public void checkWin()
    {
        Player winner = null;
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getWorld() != Bukkit.getWorld("world") && player.getGameMode() == GameMode.SURVIVAL)
            {
                winner = player;
            }
        }
        if (playerLeft == 1)
        {
            Bukkit.broadcastMessage(prefix+"§9"+winner.getDisplayName()+"§a viens de win l'UHC, GG !!!");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 10, 10);
            }
        }
    }

    public void clearEffect(Player player)
    {
        Collection<PotionEffect> effect = player.getActivePotionEffects();
        for (PotionEffect potionEffect : effect)
        {
            PotionEffectType potionType = potionEffect.getType();
            player.removePotionEffect(potionType);
        }
    }

    public void clearStuff(Player player)
    {
        player.getInventory().clear();
        ItemStack air = new ItemStack(Material.AIR);
        player.getInventory().setHelmet(air);
        player.getInventory().setChestplate(air);
        player.getInventory().setLeggings(air);
        player.getInventory().setBoots(air);
    }

    public void scoreBoard(Player player)
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("UHC-19HAuLit", "ScoreBoard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(prefix);
        int map_size = (int) Bukkit.getWorld("uhc").getWorldBorder().getSize();
        // Separator
        Score separator = objective.getScore("");
        separator.setScore(8);
        // Time
        Score time;
        if (uhcTime/60 == 0)
        {
            time = objective.getScore("§6Temps de jeu: §7" + uhcTime + "s");
        }
        else
        {
            time = objective.getScore("§6Temps de jeu: §7" + uhcTime / 60 + "min " + uhcTime % 60 + "s");
        }
        time.setScore(7);
        // Nombre de joueurs en vie
        Score alive = objective.getScore("§6Nombre de joueurs en vie: §7"+playerLeft);
        alive.setScore(6);
        // Informations de la game
        Score information = objective.getScore("§6Informations: ");
        information.setScore(5);
        Score info_1 = null;
        Score info_2 = null;
        Score info_3 = null;
        Score info_4 = null;
        info_1 = objective.getScore("§6Taille de la map §7: "+map_size);
        info_2 = objective.getScore("§6Taille de la map §7: "+map_size);
        info_3 = objective.getScore("§6Taille de la map §7: "+map_size);
        if (getConfigInt("uhc.time_pvp")-uhcTime >= 0)
        {
            if ((getConfigInt("uhc.time_pvp")-uhcTime)/60 == 0)
            {
                info_1 = objective.getScore("§6PvP: §7"+(getConfigInt("uhc.time_pvp")-uhcTime)+"s");
            }
            else
            {
                info_1 = objective.getScore("§6PvP: §7"+ (getConfigInt("uhc.time_pvp")-uhcTime)/60 +"min "+ (getConfigInt("uhc.time_pvp")-uhcTime)%60 +"s");
            }
        }
        if (getConfigInt("uhc.time_border")-uhcTime >= 0)
        {
            if ((getConfigInt("uhc.time_border")-uhcTime)/60 == 0)
            {
                info_2 = objective.getScore("§6Border: §7"+(getConfigInt("uhc.time_border")-uhcTime)+"s");
            }
            else
            {
                info_2 = objective.getScore("§6Border: §7"+ (getConfigInt("uhc.time_border")-uhcTime)/60 +"min "+ (getConfigInt("uhc.time_border")-uhcTime)%60 +"s");
            }
        }
        if (getConfigInt("uhc.time_end_border")-uhcTime >= 0)
        {
            if ((getConfigInt("uhc.time_end_border")-uhcTime)/60 == 0)
            {
                info_3 = objective.getScore("§6Meetup: §7"+(getConfigInt("uhc.time_end_border")-uhcTime)+"s");
            }
            else
            {
                info_3 = objective.getScore("§6Meetup: §7"+ (getConfigInt("uhc.time_end_border")-uhcTime)/60 +"min "+ (getConfigInt("uhc.time_end_border")-uhcTime)%60 +"s");
            }
        }
        info_4 = objective.getScore("§6Taille de la map §7: "+map_size);
        info_1.setScore(4);
        info_2.setScore(3);
        info_3.setScore(2);
        info_4.setScore(1);
        // Set du ScoreBoard à l'user
        player.setScoreboard(board);
        return;
    }

    public boolean getConfigBool(String path)
    {
        return getConfig().getBoolean(path);
    }

    public int getConfigInt(String path)
    {
        return getConfig().getInt(path);
    }

    public double getConfigDouble(String path)
    {
        return  getConfig().getDouble(path);
    }

    public void unLoadWord(World world, Location location)
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getWorld() == world)
            {
                player.setGameMode(GameMode.SURVIVAL);
                clearEffect(player);
                clearStuff(player);
                player.setHealth(20);
                player.setSaturation(20);
                player.setFoodLevel(20);
                player.teleport(location);
                player.sendMessage(prefix+"Vous venez d'être téléporter au hub car la map où vous étiez a été fermée");
            }
        }
        Bukkit.unloadWorld(world, true);
        return;
    }

    public void fileDelete(File file)
    {
        File[] contents = file.listFiles();
        if (contents != null)
        {
            for (File f : contents)
            {
                if (! Files.isSymbolicLink(f.toPath()))
                {
                    fileDelete(f);
                }
            }
        }
        file.delete();
        return;
    }


}
