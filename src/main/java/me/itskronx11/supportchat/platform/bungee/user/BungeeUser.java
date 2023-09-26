package me.itskronx11.supportchat.platform.bungee.user;

import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class BungeeUser extends User {

    public BungeeUser(UUID uniqueId) {
        super(uniqueId);
    }

    @Override
    public void sendMessage(String message) {
        ProxyServer.getInstance().getPlayer(getUniqueId()).sendMessage(message);
    }

    @Override
    public void sendMessage(BaseComponent component) {
        ProxyServer.getInstance().getPlayer(getUniqueId()).sendMessage(component);
    }

    @Override
    public void sendMessage(BaseComponent[] components) {
        ProxyServer.getInstance().getPlayer(getUniqueId()).sendMessage(components);
    }

    @Override
    public String getName() {
        return ProxyServer.getInstance().getPlayer(getUniqueId()).getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return ProxyServer.getInstance().getPlayer(getUniqueId()).hasPermission(permission);
    }

    @Override
    public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        ProxyServer.getInstance().getPlayer(getUniqueId()).sendTitle(
                ProxyServer.getInstance().createTitle()
                        .title(TextComponent.fromLegacyText(title))
                        .subTitle(TextComponent.fromLegacyText(subTitle))
                        .fadeIn(fadeIn)
                        .fadeOut(fadeOut)
                        .stay(stay)
        );


    }

}
