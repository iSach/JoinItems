package be.isach.joinitems;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sacha on 19/10/15.
 * <p>
 * Utilities about the Join Items.
 */
public class ItemsManager {

    /**
     * Loads the {@code JoinItem}s.
     *
     * @return the list of the loaded {@code JoinItem}s.
     */
    public static List<JoinItem> loadJoinItems() {
        ConfigurationSection configurationSection = JoinItems.config().getConfigurationSection("Items");
        configurationSection.getKeys(false).forEach(be.isach.joinitems.JoinItem::new);
        return JoinItem.joinItems;
    }

    /**
     * Checks if an item is a {@code JoinItem} by its ItemStack.
     *
     * @param itemStack The ItemStack of the supposed {@code JoinItem}.
     * @return {@code true} if the itemstack is a {@code JoinItem}, {@code false} otherwise.
     */
    public static boolean isJoinItem(ItemStack itemStack) {
        for (JoinItem joinItem : JoinItem.joinItems) {
            if (itemStack == null) continue;
            Material m = itemStack.getType();
            Byte data = itemStack.getData().getData();
            String name = null;
            if (itemStack.hasItemMeta()
                    && itemStack.getItemMeta().hasDisplayName())
                name = itemStack.getItemMeta().getDisplayName();
            Material m1 = itemStack.getType();
            Byte data1 = itemStack.getData().getData();
            String name1 = null;
            if (itemStack.hasItemMeta()
                    && itemStack.getItemMeta().hasDisplayName())
                name1 = itemStack.getItemMeta().getDisplayName();
            if (name != null) {
                if (m == m1
                        && data == data1
                        && name.equals(name1))
                    return true;
            } else {
                if (m == m1
                        && data == data1)
                    return true;
            }
        }
        return false;
    }

    /**
     * Gives all the {@code JoinItem}s to a player.
     *
     * @param player The receiver of the items.
     */
    public static void giveJoinItems(Player player) {
        JoinItem.joinItems.stream().filter(joinItem -> joinItem.isGivenOnJoin())
                .forEach(joinItem1 -> player.getInventory().addItem(joinItem1.getItemStack()));
    }

    /**
     * Gives all the {@code JoinItem}s to a player.
     *
     * @param player The receiver of the items.
     */
    public static void removeJoinItems(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents())
            if (itemStack != null)
                if (isJoinItem(itemStack))
                    player.getInventory().remove(itemStack);
        player.updateInventory();
    }

    /**
     * Gets a {@code JoinItem} by its ItemStack.
     *
     * @param itemStack The ItemStack of the {@code JoinItem}.
     * @return the {@code JoinItem} of the itemStack, if it's one, otherwise {@code null}
     */
    public static JoinItem getJoinItem(ItemStack itemStack) {
        for (JoinItem joinItem : JoinItem.joinItems)
            if (joinItem.getItemStack().equals(itemStack))
                return joinItem;
        return null;
    }

}
