package me.itskronx11.supportchat.user;

import lombok.Getter;
import lombok.Setter;
import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.support.Support;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public abstract class User {
    @Getter
    private final UUID uniqueId;
    @Getter
    @Setter
    private Support support;
    @Getter
    @Setter
    private Request request;
    @Getter
    @Setter
    private boolean chatEnabled;
    @Getter
    @Setter
    private boolean alertsEnabled;
    public User(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.chatEnabled = false;
        this.alertsEnabled = true;
    }
    public abstract void sendMessage(String message);
    public abstract void sendMessage(BaseComponent component);
    public abstract void sendMessage(BaseComponent[] components);
    public abstract boolean hasPermission(String permission);
    public abstract void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay);
    public abstract String getName();
}
