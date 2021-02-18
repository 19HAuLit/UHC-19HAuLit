package fr.gotta.uhc.listeners;

import fr.gotta.uhc.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


public class listeners implements Listener
{
    private Main main;

    public listeners(Main main)
    {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        event.setJoinMessage(main.prefix+player.getDisplayName()+" viens pour se battre !");
        if (main.state == "wait")
        {
            World hub = Bukkit.getWorld("world");
            Location spawn = new Location(hub, main.hub_x, main.hub_y, main.hub_z);
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(spawn);
            player.getInventory().clear();
        }
        else
            {
                Location uhc = new Location(Bukkit.getWorld("uhc"), 0, 200, 0);
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(uhc);
            }
        player.setSaturation(20);
        player.setFoodLevel(20);
        player.setHealth(20);
        return;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        World hub = Bukkit.getWorld("world");
        if(player.getWorld() == hub)
        {
            player.setSaturation(20);
            player.setFoodLevel(20);
        }
        return;
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Entity damager = event.getDamager();
            Player player = (Player) event.getEntity();
            World hub = Bukkit.getWorld("world");
            World uhc = Bukkit.getWorld("uhc");
            if (damager instanceof Player)
            {
                Player killer = (Player) damager;
                if (player.getWorld() == hub || main.state == "teleportation" || main.state == "invincible" || main.state == "game")
                {
                    damager.sendMessage(main.prefix+"Le PvP n'est pas activé !");
                    event.setCancelled(true);
                }
                else if (event.getDamage() >= player.getHealth())
                {
                    Bukkit.broadcastMessage(main.prefix + "§6" + player.getDisplayName() + "§7 viens de faire manger par §8"+ killer.getDisplayName());
                    if (player.getWorld() == uhc && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
                    {
                        main.playerLeft--;
                        main.checkWin();
                    }
                }
            }
            else if (damager instanceof Projectile)
            {
                Projectile proj = (Projectile) damager;
                if (proj.getShooter() instanceof Player)
                {
                    Player shooter = (Player) proj.getShooter();
                    if (player.getWorld() == hub || main.state == "teleportation" || main.state == "invincible" || main.state == "game")
                    {
                        shooter.sendMessage(main.prefix+"Le PvP n'est pas activé !");
                        event.setCancelled(true);
                    }
                    else if (event.getDamage() >= player.getHealth())
                        {
                            Bukkit.broadcastMessage(main.prefix +"§6"+player.getDisplayName() + "§7 viens de faire manger par §8"+ shooter.getDisplayName());
                            if (player.getWorld() == uhc && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
                            {
                                main.playerLeft--;
                                main.checkWin();
                            }
                        }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            World uhc = Bukkit.getWorld("uhc");
            World hub = Bukkit.getWorld("world");
            if (player.getWorld() == hub || main.state == "teleportation" || main.state == "invincible")
            {
                event.setCancelled(true);
            }
            else if (event.getDamage() >= player.getHealth() && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)
            {
                Bukkit.broadcastMessage(main.prefix + "§6" + player.getDisplayName() + "§7 est encore mort PvE...");
                if (player.getWorld() == uhc && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
                {
                    main.playerLeft--;
                    main.checkWin();
                }
            }
        }
        return;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage(null);
        event.getEntity().setGameMode(GameMode.SPECTATOR);
        event.getEntity().setHealth(20);
        event.getEntity().setFoodLevel(20);
        event.getEntity().setSaturation(20);
        Player player = (Player) event.getEntity();
        World uhc = Bukkit.getWorld("uhc");
        if (player.getWorld() == uhc && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
        {
            main.playerLeft--;
            main.checkWin();
        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        World hub = Bukkit.getWorld("world");
        if (player.getWorld() == hub && player.getGameMode() == GameMode.SURVIVAL)
        {
            event.setCancelled(true);
            player.sendMessage(main.prefix+"Vous ne pouvez pas drop cet item !");
        }
        return;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        World hub = Bukkit.getWorld("world");
        Block block = event.getBlock();
        Location location = block.getLocation();
        if (player.getGameMode() == GameMode.SURVIVAL)
        {
            if (player.getWorld() == hub || main.state == "teleportation")
            {
                event.setCancelled(true);
                player.sendMessage(main.prefix+"Vous ne pouvez pas casser ce block !");
            }
            else if (main.getConfigBool("scenario.cutclean"))
            {
                if (block.getType() == Material.GOLD_ORE)
                {
                    event.setCancelled(true);
                    ItemStack drop = new ItemStack(Material.GOLD_INGOT);
                    location.getWorld().dropItemNaturally(location, drop);
                    block.setType(Material.AIR);
                }
                if (block.getType() == Material.IRON_ORE)
                {
                    event.setCancelled(true);
                    ItemStack drop = new ItemStack(Material.IRON_INGOT);
                    location.getWorld().dropItemNaturally(location, drop);
                    block.setType(Material.AIR);
                }
                if (block.getType() == Material.GRAVEL)
                {
                    event.setCancelled(true);
                    ItemStack drop = new ItemStack(Material.FLINT);
                    location.getWorld().dropItemNaturally(location, drop);
                    block.setType(Material.AIR);
                }
            }
        }
        return;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        World hub = Bukkit.getWorld("world");
        if (player.getGameMode() == GameMode.SURVIVAL && player.getWorld() == hub)
        {
            event.setCancelled(true);
            player.sendMessage(main.prefix+"Vous ne pouvez pas posser ce block !");
        }
        return;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        event.setQuitMessage(main.prefix+event.getPlayer().getDisplayName()+" nous a quitté(e) lâchement...");
        Player player = event.getPlayer();
        World uhc = Bukkit.getWorld("uhc");
        if (player.getWorld() == uhc && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
        {
            main.playerLeft--;
            main.checkWin();
        }
        return;
    }
}
