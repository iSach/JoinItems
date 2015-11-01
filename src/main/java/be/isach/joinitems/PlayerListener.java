package be.isach.joinitems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

/**
 * Created by Sacha on 19/10/15.
 */
public class PlayerListener implements Listener {

    /**
     * Gives the join items on join.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Async.run(() -> {
            ItemsManager.removeJoinItems(event.getPlayer());
            ItemsManager.giveJoinItems(event.getPlayer());
        });
    }

    /**
     * Removes the join items on quit.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Async.run(() -> ItemsManager.removeJoinItems(event.getPlayer()));
    }

    /**
     * Performs command on interact.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Async.run(() -> {
            if (event.getItem() != null
                    & event.getAction() != Action.PHYSICAL) {
                if (ItemsManager.isJoinItem(event.getItem())) {
                    event.setCancelled(true);
                    Sync.run(() -> event.getPlayer().performCommand(ItemsManager.getJoinItem(event.getItem()).getCommand()));
                    event.getPlayer().updateInventory();
                    return;
                }
            }
        });
    }

    /**
     * Cancel moving JoinItem if disabled in config.
     *
     * @param event The InventoryClickEvent
     */
    @EventHandler
    public void onMoveJoinItem(InventoryClickEvent event) {
        if (event.getCurrentItem() != null
                || event.getCursor() != null) {
            if ((ItemsManager.isJoinItem(event.getCurrentItem()) || ItemsManager.isJoinItem(event.getCursor()))
                    && !JoinItem.canBeMoved) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).updateInventory();
            }
        }
    }

    /**
     * Cancel dropping JoinItem if disabled in config.
     *
     * @param event The PlayerDropItemEvent.
     */
    @EventHandler
    public void onDropJoinItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack() != null) {
            if (ItemsManager.isJoinItem(event.getItemDrop().getItemStack())
                    && !JoinItem.canBeDropped) {
                event.getItemDrop().remove();
                event.setCancelled(true);
            }
        }
    }

    /**
     * Cancel placing joinItems (if they are blocks) if it's disabled in config.
     *
     * @param event The BlockPlaceEvent.
     */
    @EventHandler
    public void onPlaceJoinItem(BlockPlaceEvent event) {
        if (event.getPlayer().getItemInHand() != null) {
            if (ItemsManager.isJoinItem(event.getPlayer().getItemInHand())
                    && !JoinItem.canBePlaced) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!JoinItem.isDroppedOnDeath) {
            Iterator<ItemStack> itemStackIterator = event.getDrops().iterator();
            while (itemStackIterator.hasNext())
                if (ItemsManager.isJoinItem(itemStackIterator.next()))
                    itemStackIterator.remove();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Async.run(() -> {
            if (JoinItem.isGivenOnRespawn)
                ItemsManager.giveJoinItems(event.getPlayer());
        });
    }

}
