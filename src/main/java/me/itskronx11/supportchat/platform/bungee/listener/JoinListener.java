package me.itskronx11.supportchat.platform.bungee.listener;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.platform.bungee.user.BungeeUser;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {
    private final SupportMain main;

    public JoinListener(SupportMain main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        this.main.getUserManager().addUser(
                new BungeeUser(e.getPlayer().getUniqueId())
        );
    }
}
