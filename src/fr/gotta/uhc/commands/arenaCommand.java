package fr.gotta.uhc.commands;

import fr.gotta.uhc.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class arenaCommand implements CommandExecutor
{
    private Main main;

    public arenaCommand(Main main)
    {
        this.main = main;
    }

    private Random random = new Random();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            World hub = Bukkit.getWorld("world");
            World arena = Bukkit.getWorld("arena");
            int map_size = (int) arena.getWorldBorder().getSize();
            if (player.getWorld() == hub || player.getGameMode() != GameMode.SURVIVAL)
            {
                // TP + CLEAR + RESISTANCE
                main.clearEffect(player);
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(20);
                player.setSaturation(20);
                player.setFoodLevel(20);
                int x = map_size/2-random.nextInt(map_size);
                int z = map_size/2-random.nextInt(map_size);
                Location location = new Location(arena, x, 256, z);
                player.teleport(location);
                player.getInventory().clear();
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10*20, 255, true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10*20, 255, true, true));
                // STUFF ARENA
                    // SWORD
                ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
                ItemMeta sword_meta = sword.getItemMeta();
                sword_meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                sword_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                sword.setItemMeta(sword_meta);
                    // BOW
                ItemStack bow = new ItemStack(Material.BOW, 1);
                ItemMeta bow_meta = bow.getItemMeta();
                bow_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                bow_meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                bow_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                bow.setItemMeta(bow_meta);
                    // WATER
                ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
                    // AXE
                ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
                ItemMeta axe_meta = axe.getItemMeta();
                axe_meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
                axe_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                axe.setItemMeta(axe_meta);
                    // FOOD
                ItemStack food = new ItemStack(Material.GOLDEN_CARROT, 64);
                    // ROD
                ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
                ItemMeta rod_meta = rod.getItemMeta();
                rod_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                rod.setItemMeta(rod_meta);
                    // GAPPLE
                ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 1);
                    // ARROW
                ItemStack arrow = new ItemStack(Material.ARROW, 1);
                    // BLOCK
                ItemStack wood = new ItemStack(Material.WOOD, 64);
                    // HELMET
                ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
                ItemMeta helmet_meta = helmet.getItemMeta();
                helmet_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                helmet_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                helmet.setItemMeta(helmet_meta);
                    // CHESTPLATE
                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
                ItemMeta chestplate_meta = chestplate.getItemMeta();
                chestplate_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                chestplate_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                chestplate.setItemMeta(chestplate_meta);
                    // LEGGINGS
                ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
                ItemMeta leggings_meta = leggings.getItemMeta();
                leggings_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                leggings_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                leggings.setItemMeta(leggings_meta);
                    // BOOTS
                ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
                ItemMeta boots_meta = boots.getItemMeta();
                boots_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                boots_meta.addEnchant(Enchantment.DURABILITY, 10, true);
                boots.setItemMeta(boots_meta);
                // SET STUFF
                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggings);
                player.getInventory().setBoots(boots);
                player.getInventory().addItem(sword, bow, wood, wood, water, axe, rod, food, gapple, arrow);
            }
            else commandSender.sendMessage(main.prefix+"Vous ne pouvez pas faire cette commande !");
        }
        else commandSender.sendMessage(main.prefix+"Vous ne pouvez pas faire cette commande !");
        return false;
    }
}
