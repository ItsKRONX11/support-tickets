package me.itskronx11.supportchat;

import me.itskronx11.supportchat.language.LanguageManager;
import me.itskronx11.supportchat.platform.spigot.language.SpigotLangManager;
import me.itskronx11.supportchat.user.UserManager;

import java.io.File;

public interface SupportMain {
    UserManager getUserManager();
    Object getConfig();
    void reloadConfig();
    File getDataFolder();
    LanguageManager getLanguageManager();
}
