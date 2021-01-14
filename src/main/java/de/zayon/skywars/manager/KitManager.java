package de.zayon.skywars.manager;

import de.zayon.zayonapi.items.Items;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

public class KitManager {

    @Getter private HashMap<Player, Kit> kitCache = new HashMap<>();
    public void setKit(Player player, Kit kit) {this.kitCache.put(player, kit);}
    public void removeKit(Player player) {this.kitCache.remove(player);}
    public Kit getCurrentKit(Player player) {return kitCache.get(player);}


    public void giveKitToPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            switch (this.kitCache.get(player)) {
                case STARTER:
                    player.getInventory().setItem(0, Items.createItem(Material.STONE_SWORD, "§7Schwert", 1));
                    player.getInventory().setItem(1, Items.createItem(Material.STONE_PICKAXE, "§7Spitzhacke", 1));
                    player.getInventory().setItem(2, Items.createItem(Material.STONE_AXE, "§7Axt", 1));
                    player.getInventory().setItem(3, Items.createItem(Material.PUMPKIN_PIE, "§7Kürbiskuchen", 8));
                    break;
                case MAURER:
                    player.getInventory().setItem(0, Items.createItem(Material.BRICK, "§7Backstein", 64));
                    player.getInventory().setItem(1, Items.createItem(Material.BRICK, "§7Backstein", 64));
                    player.getInventory().setItem(2, Items.createItem(Material.BRICK, "§7Backstein", 64));
                    player.getInventory().setHelmet(Items.createItem(Material.GOLDEN_HELMET, "§7Bauerbeiter Helm", 1));
                    break;
                case ENDERMAN:
                    player.getInventory().setItem(0, Items.createItem(Material.ENDER_PEARL, "§7Enderperle", 1));
                    player.getInventory().setBoots(Items.createEnchantment(Material.DIAMOND_BOOTS, "§7Diamantschuhe", 1, Enchantment.PROTECTION_FALL, 2));
                    break;
                case ZAUBERER:
                    player.getInventory().setItem(0, Items.createItem(Material.EXPERIENCE_BOTTLE, "§7Erfahrung", 64));
                    player.getInventory().setItem(1, Items.createItem(Material.ENCHANTING_TABLE, "§7Zaubertisch", 1));
                    player.getInventory().setItem(2, Items.createItem(Material.IRON_SWORD, "§7Eisenschwert", 1));
                    player.getInventory().setItem(3, Items.createItem(Material.LAPIS_LAZULI, "§7Lapis", 32));
                    break;
                case ENTERHACKEN:
                    player.getInventory().setItem(0, Items.createItem(Material.FISHING_ROD, "§6Enterhacken", 1));
                    player.getInventory().setItem(1, Items.createItem(Material.IRON_SWORD, "§7Eisenschwert", 1));
                    break;
                case MOERDER:
                    player.getInventory().setItem(0, Items.createEnchantment(Material.DIAMOND_SWORD, "§7Diamantschwert", 1, Enchantment.DAMAGE_ALL, 2));
                    break;
                case TANK:
                    player.getInventory().setItem(0, Items.createItem(Material.STONE_SWORD, "§7Steinschwert", 1));
                    player.getInventory().setHelmet(Items.createItem(Material.IRON_HELMET, "", 1));
                    player.getInventory().setChestplate(Items.createItem(Material.IRON_CHESTPLATE, "", 1));
                    player.getInventory().setLeggings(Items.createItem(Material.IRON_LEGGINGS, "", 1));
                    player.getInventory().setBoots(Items.createItem(Material.IRON_BOOTS, "", 1));
                    break;
                case SCHLEIM: {
                    ItemStack Schleim = new ItemStack(Material.LEATHER_CHESTPLATE);
                    LeatherArmorMeta SchleimMeta = (LeatherArmorMeta) Schleim.getItemMeta();
                    SchleimMeta.setColor(Color.fromRGB(0, 100, 0));
                    SchleimMeta.addEnchant(Enchantment.DURABILITY, 10, true);
                    SchleimMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
                    SchleimMeta.setDisplayName("§aBrustpanzer");
                    SchleimMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    Schleim.setItemMeta(SchleimMeta);

                    ItemStack Schleim1 = new ItemStack(Material.LEATHER_BOOTS);
                    LeatherArmorMeta Schleim1Meta = (LeatherArmorMeta) Schleim1.getItemMeta();
                    Schleim1Meta.setColor(Color.fromRGB(0, 100, 0));
                    Schleim1Meta.addEnchant(Enchantment.DURABILITY, 10, true);
                    Schleim1Meta.addEnchant(Enchantment.PROTECTION_FALL, 5, true);
                    Schleim1Meta.setDisplayName("§aSchuhe");
                    SchleimMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    Schleim1.setItemMeta(Schleim1Meta);

                    ItemStack potion = new ItemStack(Material.SPLASH_POTION, 3);
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.HEAL, 1, 1, true, true, true);
                    PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                    potionMeta.addCustomEffect(potionEffect, true);
                    potion.setItemMeta((ItemMeta) potionEffect);

                    player.getInventory().setItem(0, potion);
                    player.getInventory().setItem(1, Items.createItem(Material.SLIME_BLOCK, "", 32));
                    player.getInventory().setChestplate(Schleim);
                    player.getInventory().setBoots(Schleim1);
                    break;
                }
                case MINER: {
                    ItemStack Miner = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                    ItemMeta MinerMeta = Miner.getItemMeta();
                    MinerMeta.setDisplayName("§7Diamantspitzhacke");
                    MinerMeta.addEnchant(Enchantment.DIG_SPEED, 10, true);
                    MinerMeta.addEnchant(Enchantment.DURABILITY, 10, true);
                    MinerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    Miner.setItemMeta(MinerMeta);

                    ItemStack Miner1 = new ItemStack(Material.IRON_HELMET, 1);
                    ItemMeta Miner1Meta = Miner1.getItemMeta();
                    Miner1Meta.setDisplayName("§7Eisenhelm");
                    Miner1Meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                    Miner1Meta.addEnchant(Enchantment.DURABILITY, 10, true);
                    Miner1Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    Miner1.setItemMeta(Miner1Meta);

                    player.getInventory().setItem(0, Miner);
                    player.getInventory().setHelmet(Miner1);
                    break;
                }
                case BAUER: {
                    ItemStack Miner1 = new ItemStack(Material.DIAMOND_HOE, 1);
                    ItemMeta Miner1Meta = Miner1.getItemMeta();
                    Miner1Meta.setDisplayName("§7Diamanthacke");
                    Miner1Meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                    Miner1Meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                    Miner1Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    Miner1.setItemMeta(Miner1Meta);

                    player.getInventory().setItem(0, Miner1);
                    player.getInventory().setItem(1, Items.createItem(Material.HAY_BLOCK, "§cHeu", 64));
                    player.getInventory().setItem(2, Items.createItem(Material.CARROT, "§7Karotte", 16));
                    break;
                }
                case KNOCK:
                    player.getInventory().setItem(0, Items.createEnchantment(Material.BONE, "§7Knochen", 1, Enchantment.KNOCKBACK, 2));
                    break;
                case BOGENSCHUETZE:
                    player.getInventory().setItem(0, Items.createItem(Material.BOW, "§7Bogen", 1));
                    player.getInventory().setItem(1, Items.createItem(Material.ARROW, "§7Pfeil", 5));
                    break;
                case PYRO: {
                    ItemStack potion = new ItemStack(Material.POTION, 2);
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 1, true, true, true);
                    PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                    potionMeta.addCustomEffect(potionEffect, true);
                    potion.setItemMeta((ItemMeta) potionEffect);

                    player.getInventory().setItem(0, Items.createEnchantment(Material.IRON_SWORD, "§cEisenschwert", 1, Enchantment.FIRE_ASPECT, 2));
                    player.getInventory().setItem(1, potion);
                    break;
                }
                case MLG:
                    player.getInventory().setItem(0, Items.createItem(Material.WATER_BUCKET, "§7Wassereimer", 1));
                    player.getInventory().setItem(1, Items.createItem(Material.TNT, "§7Tnt", 10));
                    player.getInventory().setItem(2, Items.createItem(Material.OAK_BOAT, "§7Boot", 3));
                    player.getInventory().setItem(3, Items.createItem(Material.OAK_WOOD, "§7Holzplanke", 64));
                    player.getInventory().setItem(4, Items.createItem(Material.ENDER_PEARL, "§7Enderperle", 1));
                    break;
                case HASE: {
                    ItemStack Miner1 = new ItemStack(Material.RABBIT_FOOT, 1);
                    ItemMeta Miner1Meta = Miner1.getItemMeta();
                    Miner1Meta.setDisplayName("§7Hasenpfote");
                    Miner1Meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                    Miner1Meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
                    Miner1Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    Miner1.setItemMeta(Miner1Meta);

                    ItemStack stack0 = new ItemStack(Material.POTION, 2);
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, 6000, 2, true, true, true);
                    PotionMeta potionMeta = (PotionMeta) stack0.getItemMeta();
                    potionMeta.addCustomEffect(potionEffect, true);
                    stack0.setItemMeta((ItemMeta) potionEffect);

                    player.getInventory().setItem(0, Miner1);
                    player.getInventory().setItem(1, Items.createItem(Material.GOLDEN_CARROT, "§7Goldenekarotte", 32));
                    player.getInventory().setItem(2, stack0);
                    break;
                }
                case GEIST: {
                    ItemStack stack0 = new ItemStack(Material.POTION, 2);
                    PotionMeta pm = (PotionMeta) stack0.getItemMeta();
                    pm.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 30, 1), true);
                    pm.setDisplayName("§7Unsichtbarkeits Trank");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§7Unsichtbarkeit I ➡ 30 sek.");
                    pm.setLore(lore);
                    stack0.setItemMeta(pm);

                    player.getInventory().setItem(0, Items.createItem(Material.QUARTZ_BLOCK, "§7Quarzblock", 64));
                    player.getInventory().setItem(1, stack0);
                    break;
                }
                case ASTERIX: {
                    ItemStack stack0 = new ItemStack(Material.POTION, 2);
                    PotionMeta pm = (PotionMeta) stack0.getItemMeta();
                    pm.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 30, 1), true);
                    pm.setDisplayName("§7Stärke Trank");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§7Stärke I ➡ 30 sek.");
                    pm.setLore(lore);
                    stack0.setItemMeta(pm);

                    player.getInventory().setHelmet(Items.createItem(Material.IRON_HELMET, "§7Eisenhelm", 1));
                    player.getInventory().setItem(0, stack0);
                    break;
                }
                case SCHNEEMANN:
                    player.getInventory().setItem(0, Items.createItem(Material.SNOWBALL, "§7Schneeball", 16));
                    player.getInventory().setItem(1, Items.createItem(Material.SNOW_BLOCK, "§7Schneeblock", 2));
                    player.getInventory().setItem(2, Items.createItem(Material.IRON_SHOVEL, "§7Eisenschaufel", 1));
                    player.getInventory().setItem(3, Items.createItem(Material.PUMPKIN, "§7Kürbis", 1));
                    break;
                case PHARAO:
                    player.getInventory().setItem(0, Items.createItem(Material.GOLDEN_HELMET, "§7Goldhelm", 1));
                    player.getInventory().setItem(1, Items.createItem(Material.BEACON, "§7Leuchtfeuer", 1));
                    player.getInventory().setItem(2, Items.createItem(Material.EMERALD_BLOCK, "§7Smaragtblock", 32));
                    break;
                case SONIC: {
                    ItemStack stack0 = new ItemStack(Material.POTION, 3);
                    PotionMeta pm = (PotionMeta) stack0.getItemMeta();
                    pm.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 30, 2), true);
                    pm.setDisplayName("§7Schnelligkeits Trank");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§7Schnelligkeit II ➡ 30 sek.");
                    pm.setLore(lore);
                    stack0.setItemMeta(pm);

                    player.getInventory().setHelmet(Items.createLeatherArmor(Material.LEATHER_HELMET, "§9Sonic Helm", Color.BLUE, false, 1));
                    player.getInventory().setChestplate(Items.createLeatherArmor(Material.LEATHER_CHESTPLATE, "§9Sonic Brustplatte", Color.BLUE, false, 1));
                    player.getInventory().setLeggings(Items.createLeatherArmor(Material.LEATHER_LEGGINGS, "§9Sonic Beinschutz", Color.BLUE, false, 1));
                    player.getInventory().setBoots(Items.createLeatherArmor(Material.LEATHER_BOOTS, "§9Sonic Stiefel", Color.BLUE, false, 1));
                    player.getInventory().setItem(0, Items.createItem(Material.DIAMOND_PICKAXE, "§7Diamantspitzhacke", 1));
                    player.getInventory().setItem(1, stack0);
                    break;
                }
                case SPRENGMEISTER:
                    player.getInventory().setHelmet(Items.createLeatherArmor(Material.LEATHER_HELMET, "§cSprengmeister Helm", Color.RED, false, 1));
                    player.getInventory().setChestplate(Items.createLeatherArmor(Material.LEATHER_CHESTPLATE, "§cSprengmeister Brustplatte", Color.RED, false, 1));
                    player.getInventory().setLeggings(Items.createLeatherArmor(Material.LEATHER_LEGGINGS, "§cSprengmeister Beinschutz", Color.RED, false, 1));
                    player.getInventory().setBoots(Items.createLeatherArmor(Material.LEATHER_BOOTS, "§cSprengmeister Stiefel", Color.RED, false, 1));
                    player.getInventory().setItem(0, Items.createItem(Material.STONE_SWORD, "§7Steinschwert", 1));
                    player.getInventory().setItem(1, Items.createItem(Material.TNT, "§7Tnt", 20));
                    player.getInventory().setItem(2, Items.createItem(Material.FLINT_AND_STEEL, "§7Feuerzeug", 1));
                    player.getInventory().setItem(3, Items.createItem(Material.COBBLESTONE, "§7Bruchstein", 32));
                    break;
                case KAKTUS:
                    player.getInventory().setHelmet(Items.createEnchantment(Material.CHAINMAIL_HELMET, "§2Kaktus Helm", 1, Enchantment.THORNS, 2));
                    player.getInventory().setChestplate(Items.createEnchantment(Material.CHAINMAIL_CHESTPLATE, "§2Kaktus Brustplatte", 1, Enchantment.THORNS, 2));
                    player.getInventory().setLeggings(Items.createEnchantment(Material.CHAINMAIL_LEGGINGS, "§2Kaktus Beinschutz", 1, Enchantment.THORNS, 2));
                    player.getInventory().setBoots(Items.createEnchantment(Material.CHAINMAIL_BOOTS, "§2Kaktus Stiefel", 1, Enchantment.THORNS, 2));
                    player.getInventory().setItem(0, Items.createItem(Material.STONE_SWORD, "§7Steinschwert", 1));
                    break;
                case GOLDSACK:
                    player.getInventory().setHelmet(Items.createEnchantment(Material.GOLDEN_HELMET, "§6Gold Helm", 1, Enchantment.PROTECTION_ENVIRONMENTAL, 2));
                    player.getInventory().setChestplate(Items.createEnchantment(Material.GOLDEN_CHESTPLATE, "§6Gold Brustplatte", 1, Enchantment.PROTECTION_ENVIRONMENTAL, 2));
                    player.getInventory().setLeggings(Items.createEnchantment(Material.GOLDEN_LEGGINGS, "§6Gold Beinschutz", 1, Enchantment.PROTECTION_ENVIRONMENTAL, 2));
                    player.getInventory().setBoots(Items.createEnchantment(Material.GOLDEN_BOOTS, "§6Gold Stiefel", 1, Enchantment.PROTECTION_ENVIRONMENTAL, 2));
                    player.getInventory().setItem(0, Items.createEnchantment(Material.GOLDEN_SWORD, "§7Steinschwert", 1, Enchantment.DAMAGE_ALL, 3));
                    player.getInventory().setItem(1, Items.createItem(Material.GOLD_BLOCK, "§7Goldblock", 64));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.kitCache.get(player));
            }

        }

    }


    public enum Kit {
        GOLDSACK, KAKTUS, SPRENGMEISTER, PHARAO, SCHNEEMANN, ASTERIX, GEIST, HASE, MLG, PYRO, BOGENSCHUETZE, KNOCK, BAUER, MINER, SCHLEIM, TANK, MOERDER,
        ENTERHACKEN, ZAUBERER, ENDERMAN, MAURER, STARTER, SONIC;
    }
}


