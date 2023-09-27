package me.itskronx11.supportchat.user;

import lombok.Getter;
import lombok.Setter;
import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.support.Support;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Collection;
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
    public String getRankDisplayName() {
        String groupName = LuckPermsProvider.get().getUserManager().getUser(uniqueId).getCachedData().getMetaData().getPrimaryGroup();
        Group group = LuckPermsProvider.get().getGroupManager().getGroup(groupName);

        return ChatColor.translateAlternateColorCodes('&', group.getDisplayName());
    }
    public String getLuckPermsPrefix() {
        String prefix =  LuckPermsProvider.get().getUserManager().getUser(uniqueId).getCachedData().getMetaData().getPrefix();
        if (prefix==null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&',prefix);
    }
    public String getLuckPermsSuffix() {
        String suffix = LuckPermsProvider.get().getUserManager().getUser(uniqueId).getCachedData().getMetaData().getSuffix();
        if (suffix==null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&',suffix);
    }
    public abstract void sendMessage(String message);
    public abstract void sendMessage(BaseComponent component);
    public abstract void sendMessage(BaseComponent[] components);
    public abstract boolean hasPermission(String permission);
    public abstract Collection<String> getPermissions();
    public abstract void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay);
    public abstract String getName();
}
