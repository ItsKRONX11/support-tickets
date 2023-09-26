package me.itskronx11.supportchat.platform.bungee.language;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.language.ConfigurationWrapper;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BungeeLangManager extends ConfigManager {
    public BungeeLangManager(SupportMain main) {
        super(main);
        this.main = main;
        reload();
    }

    @Override
    public String format(User user, String s) {
        return super.format(user,s).replaceAll("%server%", ProxyServer.getInstance().getPlayer(user.getUniqueId()).getServer().getInfo().getName());
    }
    @Override
    public void reload() {
        main.reloadConfig();

        ConfigurationWrapper mainConfig =  main.getConfiguration();

        String lang = mainConfig.getString("language");

        if (lang.endsWith(".yml")) {
            lang = lang.split(".yml")[0];
        }

        File languageFile = new File(main.getDataFolder(), "language/"+lang+".yml");
        if (!languageFile.exists()) {
            System.err.println("File '"+lang+".yml' does not exist. Using default language configuration: lang_en.yml");
            languageFile = new File(main.getDataFolder(), "language/lang_en.yml");
        }
        try {
            this.config = new ConfigurationWrapper.BungeeConfig(
                    ConfigurationProvider.getProvider(YamlConfiguration.class).load(languageFile)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigurationWrapper defaultConfig = new ConfigurationWrapper.BungeeConfig(
                ConfigurationProvider.getProvider(YamlConfiguration.class).load(main.getResourceStream("language/"+lang+".yml"))
        );
        checkAndSave(defaultConfig, config, languageFile);
    }

}
