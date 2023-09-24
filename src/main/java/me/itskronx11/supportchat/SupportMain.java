package me.itskronx11.supportchat;

import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.user.UserManager;

import java.io.File;
import java.io.InputStream;

public interface SupportMain {
    UserManager getUserManager();
    Object getConfig();
    void reloadConfig();
    File getDataFolder();
    ConfigManager getLanguageManager();
    InputStream getResourceStream(String path);
    void setConfig(Object o);
}
