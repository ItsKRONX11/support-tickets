package me.itskronx11.supportchat.platform.bungee.listener;

import me.itskronx11.supportchat.SupportMain;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {
    private final SupportMain main;

    public QuitListener(SupportMain main) {
        this.main = main;
    }
    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        main.getUserManager().removeUser(
                main.getUserManager().getUser(e.getPlayer().getUniqueId())
        );
    }
}
