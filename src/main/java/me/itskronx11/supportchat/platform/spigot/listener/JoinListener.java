package me.itskronx11.supportchat.platform.spigot.listener;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.platform.spigot.user.SpigotUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private SupportMain main;
    public JoinListener(SupportMain main) {
        this.main = main;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        main.getUserManager().addUser(
                new SpigotUser(e.getPlayer().getUniqueId())
        );
    }
}
