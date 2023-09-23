package me.itskronx11.supportchat.platform.spigot.listener;

import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.platform.spigot.SupportSpigotPlugin;
import me.itskronx11.supportchat.support.Support;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    private final SupportSpigotPlugin main;
    public QuitListener(SupportSpigotPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        User user = main.getUserManager().getUser(e.getPlayer().getUniqueId());
        Support support = user.getSupport();

        if (support!=null) {
            support.removePlayer(user);
        }

    }
}
