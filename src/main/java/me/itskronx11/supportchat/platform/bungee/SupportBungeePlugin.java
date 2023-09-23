package me.itskronx11.supportchat.platform.bungee;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.LanguageManager;
import me.itskronx11.supportchat.platform.bungee.language.BungeeLangManager;
import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.user.UserManager;
import me.itskronx11.supportchat.platform.bungee.command.BungeeCommand;
import me.itskronx11.supportchat.platform.bungee.listener.ChatListener;
import me.itskronx11.supportchat.platform.bungee.listener.JoinListener;
import me.itskronx11.supportchat.platform.bungee.listener.QuitListener;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class SupportBungeePlugin extends Plugin implements SupportMain {
    private UserManager userManager;
    private LanguageManager languageManager;
    private Configuration config;

    @Override
    public void onEnable() {
        saveResource("config.yml");

        for (String s : new String[]{"en", "ro", "es"}) {
            saveResource("language/lang_"+s+".yml");
        }
        reloadConfig();

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
    }

    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public Object getConfig() {
        return config;
    }

    @Override
    public void reloadConfig() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LanguageManager getLanguageManager() {
        return languageManager;
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
