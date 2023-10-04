package me.itskronx11.supportchat.platform.bungee;

import lombok.Getter;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.language.ConfigurationWrapper;
import me.itskronx11.supportchat.platform.bungee.command.BungeeCommand;
import me.itskronx11.supportchat.platform.bungee.language.BungeeLangManager;
import me.itskronx11.supportchat.platform.bungee.listener.ChatListener;
import me.itskronx11.supportchat.platform.bungee.listener.JoinListener;
import me.itskronx11.supportchat.platform.bungee.listener.QuitListener;
import me.itskronx11.supportchat.platform.bungee.user.BungeeUser;
import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.user.UserData;
import me.itskronx11.supportchat.user.UserManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SupportBungeePlugin extends Plugin implements SupportMain {
    private UserManager userManager;
    private ConfigManager languageManager;
    private ConfigurationWrapper config;
    @Getter
    private boolean luckPerms;

    @Override
    public void onDisable() {
        userManager.saveAll();
    }
    @Override
    public void onEnable() {
        saveResource("config.yml");
        saveResource("language/lang_en.yml");

        File userDataFolder = new File(getDataFolder(), "userdata");
        if (!userDataFolder.exists()) userDataFolder.mkdirs();

        reloadConfig();

        this.luckPerms = ProxyServer.getInstance().getPluginManager().getPlugin("LuckPerms")!=null;

        languageManager = new BungeeLangManager(this);
        userManager = new UserManager(this) {
            @Override
            public User getUser(String name) {
                ProxiedPlayer player = getProxy().getPlayer(name);
                if (player==null) return null;
                return getUser(player.getUniqueId());
            }
        };
        registerEvents();

        getProxy().getPluginManager().registerCommand(this, new BungeeCommand(this, languageManager, "support"));

        getProxy().getScheduler().schedule(this, () -> userManager.saveAll(), 5, 5, TimeUnit.MINUTES);

        if (config.getBoolean("bstats")) {
            new Metrics(this, 19905);
        } else {
            getLogger().warning("Not using bStats; I would really appreciate it if you would set bstats to true in the config.yml, it's quite a big motivation for me, seeing that more and more servers are using my plugin.");
        }
    }
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public ConfigurationWrapper getConfiguration() {
        return config;
    }

    @Override
    public void reloadConfig() {
        try {
            ConfigurationWrapper config = new ConfigurationWrapper.BungeeConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml")));
            ConfigurationWrapper defaultConfig = new ConfigurationWrapper.BungeeConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(getResourceStream("config.yml")));

            ConfigManager.checkAndSave(defaultConfig, config, new File(getDataFolder(), "config.yml"));

            this.config = config;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConfigManager getLanguageManager() {
        return languageManager;
    }

    @Override
    public InputStream getResourceStream(String path) {
        return getResourceAsStream(path);
    }

    @Override
    public void setConfig(ConfigurationWrapper config) {
        this.config = config;
    }

    @Override
    public void loadUser(UUID uuid, String name) {
        UserData data = userManager.loadUserData(uuid);
        if (data==null) {
            userManager.addUser(new BungeeUser(new UserData(uuid, name, false, true, 0)));
            return;
        }
        userManager.addUser(new BungeeUser(data));
    }

    public void saveResource(String resourcePath) {
        try {
            File file = new File(getDataFolder(), resourcePath);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                InputStream in = getResourceAsStream(resourcePath);
                Files.copy(in, file.toPath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void registerEvents() {
        getProxy().getPluginManager().registerListener(this, new ChatListener(this));
        getProxy().getPluginManager().registerListener(this, new QuitListener(this));
        getProxy().getPluginManager().registerListener(this, new JoinListener(this));
    }
}
