package me.itskronx11.supportchat.user;

import lombok.Getter;
import lombok.Setter;
import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.support.Support;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public abstract class User {
    @Getter
    private final UUID uniqueId;
    @Getter
    private final String name;
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
    @Getter
    @Setter
    private int totalSupports;
    public User(UserData data) {
        this.uniqueId = data.getUuid();
        this.chatEnabled = data.isChat();
        this.alertsEnabled = data.isAlerts();
        this.totalSupports = data.getTotalSupports();
        this.name = data.getName();
    }
    public String getRankDisplayName() {
        String groupName = LuckPermsProvider.get().getUserManager().getUser(uniqueId).getCachedData().getMetaData().getPrimaryGroup();
        Group group = LuckPermsProvider.get().getGroupManager().getGroup(groupName);

        return ChatColor.translateAlternateColorCodes('&', group.getDisplayName());
    }
    public String getLuckPermsPrefix() {
        String prefix =  LuckPermsProvider.get().getUserManager().getUser(uniqueId).getCachedData().getMetaData().getPrefix();
        return (prefix==null) ?"":ChatColor.translateAlternateColorCodes('&',prefix);
    }
    public String getLuckPermsSuffix() {
        String suffix = LuckPermsProvider.get().getUserManager().getUser(uniqueId).getCachedData().getMetaData().getSuffix();
        return (suffix==null) ?"": ChatColor.translateAlternateColorCodes('&',suffix);
    }
    public abstract void sendMessage(String message);
    public abstract void sendMessage(BaseComponent component);
    public abstract void sendMessage(BaseComponent[] components);
    public abstract boolean hasPermission(String permission);
    public abstract void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay);
    public UserData getSaveData() {
        return new UserData(uniqueId, name, chatEnabled, alertsEnabled, totalSupports);
    }
}
