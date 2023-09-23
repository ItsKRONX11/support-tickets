package me.itskronx11.supportchat.user;

import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.support.Support;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public interface User {
    void sendMessage(String message);
    void sendMessage(BaseComponent component);
    void sendMessage(BaseComponent[] components);
    Support getSupport();
    void setSupport(Support support);
    boolean isChatEnabled();
    void setChatEnabled(boolean chatEnabled);
    boolean isAlertsEnabled();
    void setAlertsEnabled(boolean alertsEnabled);
    Request getRequest();
    void setRequest(Request request);
    String getName();
    UUID getUniqueId();
    boolean hasPermission(String permission);

}
