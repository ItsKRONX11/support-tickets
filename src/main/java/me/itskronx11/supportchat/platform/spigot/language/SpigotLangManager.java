package me.itskronx11.supportchat.platform.spigot.language;

import me.clip.placeholderapi.PlaceholderAPI;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.language.ConfigurationWrapper;
import me.itskronx11.supportchat.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;

public final class SpigotLangManager extends ConfigManager {
    public SpigotLangManager(SupportMain main) {
        super(main);

        reload();
    }
    @Override
    public void reload() {
        main.reloadConfig();

        String lang = ( main.getConfiguration()).getString("language");

        if (lang.endsWith(".yml")) {
            lang = lang.split(".yml")[0];
        }

        File file = new File(main.getDataFolder().getPath()+"/language/"+lang+".yml");
        if (!file.exists()) {
            System.err.println("File '"+lang+".yml' does not exist. Using default language configuration: lang_en.yml");
            file = new File(main.getDataFolder(), "language/lang_en.yml");
        }

        config = new ConfigurationWrapper.SpigotConfig(
                YamlConfiguration.loadConfiguration(file)
        );

        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(main.getResourceStream("language/lang_en.yml")));

        ConfigManager.checkAndSave(new ConfigurationWrapper.SpigotConfig(defaultConfig), config, file);
    }
    @Override
    public String format(User user, String s) {
        if (main.isPlaceholderAPI()) {
            return PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(user.getUniqueId()), super.format(user, s));
        }
        return super.format(user, s);
    }

}
