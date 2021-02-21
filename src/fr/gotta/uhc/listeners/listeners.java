package fr.gotta.uhc.listeners;

import fr.gotta.uhc.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;


public class listeners implements Listener
{
    private Main main;

    public listeners(Main main)
    {
        this.main = main;
    }

    private final Random rand = new Random();

    private String DeathMsg = null;

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        main.scoreBoard(player);
        World uhc = Bukkit.getWorld("uhc");
        event.setJoinMessage(main.prefix+player.getDisplayName()+" viens pour se battre !");
        World hub = Bukkit.getWorld("world");
        if (main.state == "wait")
        {
            Location spawn = new Location(hub, main.hub_x, main.hub_y, main.hub_z);
            player.setGameMode(GameMode.SURVIVAL);
            player.getActivePotionEffects().clear();
            player.teleport(spawn);
            player.getInventory().clear();
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.setHealth(20);
            player.setExp(0);
            player.setLevel(0);
        }
        else if (player.getWorld() != hub && player.getGameMode() == GameMode.SURVIVAL)
        {
            main.playerLeft++;
        }
        else
        {
            Location specUhc = new Location(uhc, 0, 200, 0);
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(specUhc);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.setHealth(20);
        }
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
    public void Portal(PlayerPortalEvent event)
    {
        Player player = event.getPlayer();
        World nether = Bukkit.getWorld("uhc_nether");
        World uhc = Bukkit.getWorld("uhc");
        World world = nether;
        if (player.getWorld() == nether) world = uhc;
        Location location = new Location(world, player.getLocation().getBlockX()/2.0, player.getLocation().getBlockY(), player.getLocation().getBlockZ()/2.0);
        event.useTravelAgent(true);
        event.getPortalTravelAgent().setCanCreatePortal(true);
        Location portalLoc = event.getPortalTravelAgent().findOrCreate(location);
        Location loc = new Location(portalLoc.getWorld(), portalLoc.getBlockX()+0.5, portalLoc.getBlockY(), portalLoc.getBlockZ()+0.5);
        player.teleport(loc);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Entity damager = event.getDamager();
            Player player = (Player) event.getEntity();
            World hub = Bukkit.getWorld("world");
            if (damager instanceof Player)
            {
                Player killer = (Player) damager;
                if (player.getWorld() == hub || main.state == "teleportation" || main.state == "invincible" || main.state == "game")
                {
                    damager.sendMessage(main.prefix+"Le PvP n'est pas activé !");
                    event.setCancelled(true);
                }
                DeathMsg = main.prefix + "§6" + player.getDisplayName() + "§7 vient de faire manger par §8"+ killer.getDisplayName();
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
                            DeathMsg = main.prefix + "§6" + player.getDisplayName() + "§7 vient de faire manger par §8"+ shooter.getDisplayName();
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
            World hub = Bukkit.getWorld("world");
            if (player.getWorld() == hub || main.state == "teleportation" || main.state == "invincible")
            {
                event.setCancelled(true);
            }
            else if (event.getDamage() >= player.getHealth() && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)
            {
                DeathMsg = main.prefix + "§6" + player.getDisplayName() + "§7 est encore mort §8PvE...";
            }
            if (main.getConfigBool("scenario.fireless"))
            {
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.FIRE)
                {
                    event.setCancelled(true);
                }
            }
            if (main.getConfigBool("scenario.nofall"))
            {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);
            }
        }
        return;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        Entity entity = event.getEntity();
        Location location = event.getEntity().getLocation();
        if (entity instanceof Cow)
        {
            //location.getWorld().dropItemNaturally(location, new ItemStack(Material.COOKED_BEEF, 1));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        Player player = (Player) event.getEntity();
        World hub = Bukkit.getWorld("world");
        if (player.getWorld() != hub && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
        {
            event.setDeathMessage(null);
            Bukkit.broadcastMessage(DeathMsg);
            event.getEntity().setGameMode(GameMode.SPECTATOR);
            event.getEntity().setHealth(20);
            event.getEntity().setFoodLevel(20);
            event.getEntity().setSaturation(20);
            main.playerLeft--;
            main.checkWin();
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (main.bukkit_version.startsWith("1.8"))
                {
                    p.playSound(player.getLocation(), Sound.EXPLODE, 10, 10);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event)
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
    public void onLeavesDrop(LeavesDecayEvent event)
    {
        Block block = event.getBlock();
        Location location = block.getLocation();
        if (main.getConfigBool("scenario.all_tree_drop"))
        {
            if (block.getType() == Material.LEAVES)
            {
                double rdmDrop = rand.nextDouble() * 100;
                if (rdmDrop <= main.getConfigDouble("scenario.apple_rate"))
                {
                    ItemStack drop = new ItemStack(Material.APPLE);
                    location.getWorld().dropItemNaturally(location, drop);
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        World hub = Bukkit.getWorld("world");
        Block block = event.getBlock();
        Location location = block.getLocation();
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL)
        {
            if (player.getWorld() == hub || main.state == "teleportation")
            {
                event.setCancelled(true);
                player.sendMessage(main.prefix+"Vous ne pouvez pas casser ce block !");
            }
            if (main.getConfigBool("scenario.cutclean") && player.getWorld() != hub && main.state != "teleportation")
            {
                if (block.getType() == Material.GOLD_ORE)
                {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    ItemStack drop = new ItemStack(Material.GOLD_INGOT);
                    location.getWorld().dropItemNaturally(location, drop);
                    player.giveExp(main.getConfigInt("scenario.xp_rate"));
                }
                if (block.getType() == Material.IRON_ORE)
                {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    ItemStack drop = new ItemStack(Material.IRON_INGOT);
                    location.getWorld().dropItemNaturally(location, drop);
                    player.giveExp(main.getConfigInt("scenario.xp_rate"));
                }
                if (block.getType() == Material.GRAVEL)
                {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    ItemStack drop = new ItemStack(Material.FLINT);
                    location.getWorld().dropItemNaturally(location, drop);
                }
            }
            if (main.getConfigBool("scenario.all_tree_drop") && player.getWorld() != hub && main.state != "teleportation")
            {
                if (block.getType() == Material.LEAVES)
                {
                    double rdmDrop = rand.nextDouble() * 100;
                    if (rdmDrop <= main.getConfigDouble("scenario.apple_rate"))
                    {
                        ItemStack drop = new ItemStack(Material.APPLE);
                        block.getDrops().add(drop);
                        //location.getWorld().dropItemNaturally(location, drop);
                    }
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
    public void Server(ServerListPingEvent event)
    {
        event.setMotd(main.getConfigString("server.modt"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        event.setQuitMessage(main.prefix+event.getPlayer().getDisplayName()+" nous a quitté(e) lâchement...");
        Player player = event.getPlayer();
        World hub = Bukkit.getWorld("hub");
        if (player.getWorld() != hub && player.getGameMode() == GameMode.SURVIVAL && main.state != "wait")
        {
            main.playerLeft--;
            main.checkWin();
        }
        return;
    }
}
