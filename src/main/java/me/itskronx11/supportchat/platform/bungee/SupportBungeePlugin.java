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
import me.itskronx11.supportchat.user.User;
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

public class SupportBungeePlugin extends Plugin implements SupportMain {
    private UserManager userManager;
    private ConfigManager languageManager;
    private ConfigurationWrapper config;
    @Getter
    private boolean luckPerms;

    @Override
    public void onEnable() {
        saveResource("config.yml");
        saveResource("language/lang_en.yml");

        reloadConfig();

        this.luckPerms = ProxyServer.getInstance().getPluginManager().getPlugin("LuckPerms")!=null;

        languageManager = new BungeeLangManager(this);
        userManager = new UserManager() {
            @Override
            public User getUser(String name) {
                ProxiedPlayer player = getProxy().getPlayer(name);
                if (player==null) return null;

                return getUser(player.getUniqueId());
            }
        };

        getProxy().getPluginManager().registerListener(this, new ChatListener(this));
        getProxy().getPluginManager().registerListener(this, new QuitListener(this));
        getProxy().getPluginManager().registerListener(this, new JoinListener(this));

        getProxy().getPluginManager().registerCommand(this, new BungeeCommand(this, languageManager, "support"));

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
}
