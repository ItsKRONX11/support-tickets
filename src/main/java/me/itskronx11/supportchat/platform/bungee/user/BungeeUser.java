package me.itskronx11.supportchat.platform.bungee.user;

import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.support.Support;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class BungeeUser implements User {
    private final UUID uuid;
    private Support support;
    private Request request;
    private boolean chatEnabled;
    private boolean alertsEnabled;

    public BungeeUser(UUID uuid) {
        this.uuid = uuid;

        chatEnabled = false;
        alertsEnabled = true;
    }

    @Override
    public void sendMessage(String message) {
        ProxyServer.getInstance().getPlayer(uuid).sendMessage(message);
    }

    @Override
    public void sendMessage(BaseComponent component) {
        ProxyServer.getInstance().getPlayer(uuid).sendMessage(component);
    }

    @Override
    public void sendMessage(BaseComponent[] components) {
        ProxyServer.getInstance().getPlayer(uuid).sendMessage(components);
    }

    @Override
    public Support getSupport() {
        return support;
    }

    @Override
    public void setSupport(Support support) {
        this.support = support;
    }

    @Override
    public boolean isChatEnabled() {
        return chatEnabled;
    }

    @Override
    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    @Override
    public boolean isAlertsEnabled() {
        return alertsEnabled;
    }

    @Override
    public void setAlertsEnabled(boolean alertsEnabled) {
        this.alertsEnabled = alertsEnabled;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public String getName() {
        return ProxyServer.getInstance().getPlayer(uuid).getName();
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public boolean hasPermission(String permission) {
        return ProxyServer.getInstance().getPlayer(uuid).hasPermission(permission);
    }

    @Override
    public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        ProxyServer.getInstance().getPlayer(uuid).sendTitle(
                ProxyServer.getInstance().createTitle()
                        .title(TextComponent.fromLegacyText(title))
                        .subTitle(TextComponent.fromLegacyText(subTitle))
                        .fadeIn(fadeIn)
                        .fadeOut(fadeOut)
                        .stay(stay)
        );


    }

}
