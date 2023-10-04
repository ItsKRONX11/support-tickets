package me.itskronx11.supportchat.platform.bungee.listener;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {
    private final SupportMain main;

    public QuitListener(SupportMain main) {
        this.main = main;
    }
    @EventHandler
    public void onQuit(final PlayerDisconnectEvent e) {
        User user = main.getUserManager().getUser(e.getPlayer().getUniqueId());

        main.getUserManager().save(user);

        if (user.getSupport()!=null) user.getSupport().removePlayer(user);

        main.getUserManager().removeUser(user);
        
    }
}
