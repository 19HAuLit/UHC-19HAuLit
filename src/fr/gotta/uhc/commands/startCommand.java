package fr.gotta.uhc.commands;

import fr.gotta.uhc.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class startCommand implements CommandExecutor
{
    private Main main;

    public startCommand(Main main)
    {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (main.state != "wait")
        {
            commandSender.sendMessage(main.prefix+"Le serveur doit reload ou restart pour pouvoir faire cette commande !");
            return false;
        }
        main.state = "teleportation";
        Bukkit.broadcastMessage(main.prefix+"Téléportation des joueurs");
        // BORDER SET
        int map_size = main.getConfigInt("uhc.map_size");
        World uhc = Bukkit.getWorld("uhc");
        WorldBorder border = uhc.getWorldBorder();
        border.setCenter(0, 0);
        border.setSize(map_size);
        border.setDamageAmount(1);
        border.setDamageBuffer(1);
        border.setWarningDistance(0);
        uhc.setTime(0);
        uhc.setGameRuleValue("naturalRegeneration","false");
        // RTP Players on uhc map
        Random rand = new Random();
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE);
        ItemStack axe = new ItemStack(Material.STONE_AXE);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        ItemStack book = new ItemStack(Material.BOOK, 3);
        for(Player player : Bukkit.getOnlinePlayers())
        {
            int x = map_size/2-rand.nextInt(map_size);
            int z = map_size/2-rand.nextInt(map_size);
            Location location = new Location(uhc,x, 256, z);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.getInventory().addItem(sword, pickaxe, axe, food, book);
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.setHealth(20);
            player.getActivePotionEffects().clear();
            player.setExp(0);
            player.setLevel(0);
            player.teleport(location);
            main.playerLeft++;
        }
        // Invincible
        main.state = "invincible";
        main.getServer().getScheduler().runTaskTimer(main, new Runnable()
        {
            int time = 0;
            int time_invincible = main.getConfigInt("uhc.time_invincible");
            int time_final_heal = main.getConfigInt("uhc.time_final_heal");
            int time_pvp = main.getConfigInt("uhc.time_pvp");
            int time_border = main.getConfigInt("uhc.time_border");
            int time_end_border = main.getConfigInt("uhc.time_end_border");
            @Override
            public void run()
            {
                // Invincible to Game
                if (time_invincible-time > 0)
                {
                    if (time_invincible-time == 60 || time_invincible-time == 60*2 || time_invincible-time == 60*3 || time_invincible-time == 60*4 || time_invincible-time == 60*5 || time_invincible-time == 60*10 || time_invincible-time == 60*20)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Fin de l'invincibilité dans "+(time_invincible-time)/60+" minutes");
                    }
                    else if (time_invincible-time < 6)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Fin de l'invincibilité dans "+(time_invincible-time)+" secondes");
                    }
                }
                if (time == time_invincible)
                {
                    Bukkit.broadcastMessage(main.prefix+"Fin de l'invincibilité !");
                    main.state = "game";
                }
                // Final Heal
                if (time_final_heal-time > 0)
                {
                    if (time_final_heal-time == 60 || time_final_heal-time == 60*2 || time_final_heal-time == 60*3 || time_final_heal-time == 60*4 || time_final_heal-time == 60*5 || time_final_heal-time == 60*10 || time_final_heal-time == 60*20)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Final Heal dans "+(time_final_heal-time)/60+" minutes");
                    }
                    else if (time_final_heal-time < 6)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Final Heal dans "+(time_final_heal-time)+" secondes");
                    }
                }
                if (time == time_final_heal)
                {
                    Bukkit.broadcastMessage(main.prefix+"Final Heal !");
                    for(Player player : Bukkit.getOnlinePlayers())
                    {
                        player.setHealth(player.getHealthScale());
                    }

                }
                // Game to PvP
                if (time_pvp-time > 0)
                {
                    if (time_pvp-time == 60 || time_pvp-time == 60*2 || time_pvp-time == 60*3 || time_pvp-time == 60*4 || time_pvp-time == 60*5 || time_pvp-time == 60*10 || time_pvp-time == 60*20)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Début du PvP dans "+(time_pvp-time)/60+" minutes");
                    }
                    else if (time_pvp-time < 6)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Début du PvP dans "+(time_pvp-time)+" secondes");
                    }
                }
                if (time == time_pvp)
                {
                    Bukkit.broadcastMessage(main.prefix+"PvP activé !");
                    main.state = "pvp";
                }
                // PvP to Border
                if (time_border-time > 0)
                {
                    if (time_border-time == 60 || time_border-time == 60*2 || time_border-time == 60*3 || time_border-time == 60*4 || time_border-time == 60*5 || time_border-time == 60*10 || time_border-time == 60*20)
                    {
                        Bukkit.broadcastMessage(main.prefix+"La border commence à bouger dans "+(time_border-time)/60+" minutes");
                    }
                    else if (time_border-time < 6)
                    {
                        Bukkit.broadcastMessage(main.prefix+"La border commence à bouger dans "+(time_border-time)+" secondes");
                    }
                }
                if (time == time_border)
                {
                    Bukkit.broadcastMessage(main.prefix+"La border commence à bouger !");
                    main.state = "border";
                    int time_to_moove = time_end_border - time_border;
                    uhc.getWorldBorder().setSize(main.getConfigInt("uhc.final_map_size"), time_to_moove);
                }
                // Border to End
                if (time_end_border-time > 0)
                {
                    if (time_end_border-time == 60 || time_end_border-time == 60*2 || time_end_border-time == 60*3 || time_end_border-time == 60*4 || time_end_border-time == 60*5 || time_end_border-time == 60*10 || time_end_border-time == 60*20)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Fin du mouvement de la border dans "+(time_end_border-time)/60+" minutes");
                    }
                    else if (time_end_border-time < 6)
                    {
                        Bukkit.broadcastMessage(main.prefix+"Fin du mouvement de la border dans "+(time_end_border-time)+" secondes");
                    }
                }
                if (time == time_end_border)
                {
                    Bukkit.broadcastMessage(main.prefix+"La border a fini de bouger !");
                    main.state = "end";
                }
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    main.scoreBoard(player);
                }
                main.uhcTime++;
                time++;
                return;
            }
        },0, 20);
        return false;
    }
}
