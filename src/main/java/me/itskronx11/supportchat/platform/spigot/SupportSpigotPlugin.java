package me.itskronx11.supportchat.platform.spigot;

import lombok.Getter;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.language.ConfigurationWrapper;
import me.itskronx11.supportchat.platform.spigot.command.SpigotCommand;
import me.itskronx11.supportchat.platform.spigot.language.SpigotLangManager;
import me.itskronx11.supportchat.platform.spigot.listener.ChatListener;
import me.itskronx11.supportchat.platform.spigot.listener.JoinListener;
import me.itskronx11.supportchat.platform.spigot.listener.QuitListener;
import me.itskronx11.supportchat.platform.spigot.user.SpigotUser;
import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.user.UserData;
import me.itskronx11.supportchat.user.UserManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public final class SupportSpigotPlugin extends JavaPlugin implements SupportMain, Listener {
    @Getter
    private SpigotLangManager languageManager;
    private UserManager userManager;
    @Getter
    private ConfigurationWrapper configuration;
    @Getter
    boolean luckPerms;
    boolean placeholderAPI;

    @Override
    public void onDisable() {
        userManager.saveAll();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("language/lang_en.yml", false);

        File userDataFolder = new File(getDataFolder(), "userdata");
        if (!userDataFolder.exists()) userDataFolder.mkdirs();

        this.luckPerms = Bukkit.getPluginManager().getPlugin("LuckPerms")!=null;
        this.placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null;

        configuration = new ConfigurationWrapper.SpigotConfig(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")));
        languageManager = new SpigotLangManager(this);
        userManager = new UserManager(this) {
            @Override
            public User getUser(String name) {
                Player player =  Bukkit.getPlayer(name);
                if (player==null) return null;
                return getUser(player.getUniqueId());
            }

        };

        getCommand("support").setExecutor(new SpigotCommand(this));

        registerEvents();
        bStats();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> userManager.saveAll(), 6000, 6000);

        for (Player player : Bukkit.getOnlinePlayers())  { // If this is a reload, make sure all the players still have a User object assigned
            loadUser(player.getUniqueId(), player.getName());
        }
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
    public void setConfig(ConfigurationWrapper o) {
        this.configuration = o;
    }
    @Override
    public void reloadConfig() {
        ConfigurationWrapper config = new ConfigurationWrapper.SpigotConfig(YamlConfiguration.loadConfiguration(new File(getDataFolder(),"config.yml")));
        ConfigurationWrapper defaultConfig = new ConfigurationWrapper.SpigotConfig(YamlConfiguration.loadConfiguration(new InputStreamReader(getResourceStream("config.yml"))));

        ConfigManager.checkAndSave(defaultConfig, config, new File(getDataFolder(), "config.yml"));
        this.configuration = config;
    }
    private void bStats() {
        if (configuration.getBoolean("bstats")) {
            new Metrics(this, 19904);
        } else {
            getLogger().warning("Not using bStats; I would really appreciate it if you would set bstats to true in the config.yml, it's quite a big motivation for me, seeing that more and more servers are using my plugin.");
        }
    }
    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new QuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
    }
    public void loadUser(UUID uuid, String name){
        UserData data = userManager.loadUserData(uuid);
        if (data==null) {
            userManager.addUser(new SpigotUser(new UserData(uuid, name, false, true, 0)));
            return;
        }
        userManager.addUser(new SpigotUser(data));
    }
    public boolean isPlaceholderAPI() {
        return placeholderAPI;
    }

}
