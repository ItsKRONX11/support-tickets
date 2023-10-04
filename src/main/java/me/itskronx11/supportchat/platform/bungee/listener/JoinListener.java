package me.itskronx11.supportchat.platform.bungee.listener;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.platform.bungee.user.BungeeUser;
import me.itskronx11.supportchat.user.UserData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {
    private final SupportMain main;

    public JoinListener(SupportMain main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(final PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();

            UserData data = main.getUserManager().loadUserData(player.getUniqueId());
            if (data==null) {
                main.getUserManager().addUser(new BungeeUser(new UserData(player.getUniqueId(), player.getName(), false, true, 0)));
                return;
            }
            main.getUserManager().addUser(new BungeeUser(data));
    }
}
