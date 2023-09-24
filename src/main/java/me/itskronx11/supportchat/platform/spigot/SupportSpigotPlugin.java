package me.itskronx11.supportchat.platform.spigot;

import lombok.Getter;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.platform.spigot.command.SpigotCommand;
import me.itskronx11.supportchat.platform.spigot.language.SpigotLangManager;
import me.itskronx11.supportchat.platform.spigot.listener.ChatListener;
import me.itskronx11.supportchat.platform.spigot.listener.JoinListener;
import me.itskronx11.supportchat.platform.spigot.listener.QuitListener;
import me.itskronx11.supportchat.platform.spigot.user.SpigotUser;
import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

public final class SupportSpigotPlugin extends JavaPlugin implements SupportMain {
    @Getter
    private SpigotLangManager languageManager;
    private UserManager userManager;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        for (String s : new String[]{"en","ro", "es"}) {
            saveResource("language/lang_"+s+".yml", false);
        }

        config = YamlConfiguration.loadConfiguration(new File("config.yml"));

        userManager = new UserManager() {
            @Override
            public User getUser(String name) {
                Player player =  Bukkit.getPlayerExact(name);
                if (player==null) return null;
                return getUser(player.getUniqueId());
            }

        };

        languageManager = new SpigotLangManager(this);

        getCommand("support").setExecutor(new SpigotCommand(this));

        Bukkit.getPluginManager().registerEvents(new QuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);

        Bukkit.getOnlinePlayers().forEach(player -> userManager.addUser(new SpigotUser(player.getUniqueId())));
    }
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public InputStream getResourceStream(String path) {
        return getResource(path);
    }

    @Override
    public void setConfig(Object o) {
        this.config = (FileConfiguration) o;
    }
    @Override
    public FileConfiguration getConfig() {
        return config;
    }
    @Override
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"config.yml"));
    }

}
