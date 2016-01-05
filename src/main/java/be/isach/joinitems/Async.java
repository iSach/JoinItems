package be.isach.joinitems;

import org.bukkit.Bukkit;

/**
 * Created by Sacha on 19/10/15.
 *
 * Runs easily stuff in async cause 
 * I am too lazy to type this each time lol
 *
 */
public class Async {

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(JoinItems.instance, runnable);
    }

}
