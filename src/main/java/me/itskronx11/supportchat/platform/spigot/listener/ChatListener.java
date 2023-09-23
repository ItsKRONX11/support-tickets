package me.itskronx11.supportchat.platform.spigot.listener;

import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.platform.spigot.SupportSpigotPlugin;
import me.itskronx11.supportchat.support.Support;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final SupportSpigotPlugin main;
    public ChatListener(SupportSpigotPlugin main) {
        this.main = main;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        User user = main.getUserManager().getUser(e.getPlayer().getUniqueId());
        Support support = user.getSupport();

        if (!user.isChatEnabled() || support==null) return;

        e.setCancelled(true);

        support.sendMessage(main.getLanguageManager().getMessage(user, "support-chat").replaceAll("%message%", e.getMessage()));
    }
}
