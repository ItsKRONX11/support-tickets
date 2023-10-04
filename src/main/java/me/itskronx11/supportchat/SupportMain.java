package me.itskronx11.supportchat;

import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.language.ConfigurationWrapper;
import me.itskronx11.supportchat.user.UserManager;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

public interface SupportMain {
    UserManager getUserManager();
    ConfigurationWrapper getConfiguration();
    void reloadConfig();
    File getDataFolder();
    ConfigManager getLanguageManager();
    InputStream getResourceStream(String path);
    void setConfig(ConfigurationWrapper config);
    boolean isLuckPerms();
    void loadUser(UUID uuid, String name);
}
