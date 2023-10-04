package me.itskronx11.supportchat.platform.bungee.listener;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.support.Support;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
    private SupportMain main;
    public ChatListener(SupportMain main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(final ChatEvent e) {
        if (e.getMessage().startsWith("/")) return;

        User user = main.getUserManager().getUser( ((ProxiedPlayer) e.getSender()).getUniqueId());
        Support support = user.getSupport();

        if (support == null || !user.isChatEnabled()) return;
        e.setCancelled(true);

        support.sendMessage(main.getLanguageManager().getMessage(user, "support-chat").replaceAll("%message%", e.getMessage()));
    }
}
