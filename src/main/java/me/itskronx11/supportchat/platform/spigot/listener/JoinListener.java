package me.itskronx11.supportchat.platform.spigot.listener;

import me.itskronx11.supportchat.SupportMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final SupportMain main;
    public JoinListener(SupportMain main) {
        this.main = main;
    }
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        main.loadUser(e.getPlayer().getUniqueId(), e.getPlayer().getName());
    }
}
