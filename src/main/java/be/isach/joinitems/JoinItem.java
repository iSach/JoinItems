package be.isach.joinitems;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sacha on 19/10/15.
 */
public class JoinItem {

    public static List<JoinItem> joinItems = new ArrayList<>();

    private String command;
    private String displayName;
    private List<String> lore;
    private Material material;
    private int amount;
    private byte data;
    private int slot;
    private boolean giveOnJoin;
    private ItemStack itemStack;

    public static boolean canBeDropped;
    public static boolean canBeMoved;
    public static boolean canBePlaced;
    public static boolean isGivenOnRespawn;
    public static boolean isDroppedOnDeath;

    public JoinItem(String pathName) {

        try {
            String completePath = "Items." + pathName;
            this.command = JoinItems.getString(completePath + ".command", false);
            this.displayName = JoinItems.getString(completePath + ".display-name", true);
            this.lore = (List<String>) JoinItems.config().getList(completePath + ".lore");
            this.material = Material.getMaterial(JoinItems.getString(completePath + ".material", false));
            this.data = (byte) JoinItems.config().getInt(completePath + ".data");
            this.amount = JoinItems.config().getInt(completePath + ".amount");
            this.slot = JoinItems.config().getInt(completePath + ".slot") - 1;
            this.giveOnJoin = JoinItems.config().getBoolean(completePath + ".give-on-join");

            List<String> finalLore = new ArrayList<>();

            lore.forEach(s -> finalLore.add(ChatColor.translateAlternateColorCodes('&', s)));

            lore = finalLore;

            itemStack = makeItemStack();

            joinItems.add(this);

            Bukkit.getServer().getConsoleSender().sendMessage("§b§l§o[JoinItems]: §b§lSuccessfuly loaded item: §n" + pathName);
        } catch (Exception exc) {
            Bukkit.getServer().getConsoleSender().sendMessage("§c§l-------------------");
            Bukkit.getServer().getConsoleSender().sendMessage("§c§lJoinItems: Invalid item: " + pathName);
            Bukkit.getServer().getConsoleSender().sendMessage("§c§l-------------------");
        }
    }

    private ItemStack makeItemStack() {
        ItemStack itemStack = new MaterialData(material, data).toItemStack(amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isGivenOnJoin() {
        return giveOnJoin;
    }

    public int getSlot() {
        return slot;
    }

    public String getCommand() {
        return command;
    }
}
