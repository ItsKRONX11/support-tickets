package me.itskronx11.supportchat.platform.spigot.user;

import com.cryptomorin.xseries.messages.Titles;
import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.support.Support;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;

import java.util.UUID;

public class SpigotUser implements User {
    private final UUID uuid;
    private final String name;
    private Request request;
    private Support support;
    private boolean chatEnabled;
    private boolean alertsEnabled;
    public SpigotUser(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();

        chatEnabled = false;
        alertsEnabled = true;
    }
    @Override
    public void sendMessage(String message) {
        Bukkit.getPlayer(uuid).sendMessage(message);
    }

    @Override
    public void sendMessage(BaseComponent component) {
        Bukkit.getPlayer(uuid).spigot().sendMessage(component);
    }

    @Override
    public void sendMessage(BaseComponent[] components) {
        Bukkit.getPlayer(uuid).spigot().sendMessage(components);
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
        return name;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public boolean hasPermission(String permission) {
        return Bukkit.getPlayer(uuid).hasPermission(permission);
    }

    @Override
    public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        Titles.sendTitle(
                Bukkit.getPlayer(uuid),
                fadeIn,
                stay,
                fadeOut,
                title,
                subTitle);
    }


}
