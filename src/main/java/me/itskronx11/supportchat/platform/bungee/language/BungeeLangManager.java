package me.itskronx11.supportchat.platform.bungee.language;

import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.LanguageManager;
import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BungeeLangManager implements LanguageManager {
    private final SupportMain main;
    private Configuration config;
    private String[] usage;
    public BungeeLangManager(SupportMain main) {
        this.main = main;

        reload();
    }

    @Override
    public String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s).replaceAll("%prefix%", getPrefix()).replaceAll("%nl%", "\n");
    }

    @Override
    public String format(User user, String s) {
        return format(s).replaceAll("%name%", user.getName());
    }

    @Override
    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&',((Configuration) main.getConfig()).getString("prefix"));
    }

    @Override
    public String getMessage(String path) {
        return format(config.getString(path)).replaceAll("%nl%", "\n");
    }

    @Override
    public String getMessage(User user, String path) {
        return format(user, config.getString(path));
    }

    @Override
    public String[] getUsage() {
        if (usage==null) {
            List<String > configList = config.getStringList("usage");
            String[] use = new String[configList.size()];

            for (String s : configList) {
                use[configList.indexOf(s)] = format(s);
            }
            usage = use;
        }
        return usage;
    }

    @Override
    public List<TextComponent> getSupportAlert(User sender, String reason) {
        List<TextComponent> alert = new ArrayList<>();

        for (String line : config.getStringList("support-alert")) {
            line = format(sender, line).replaceAll("%reason%", reason);

            int start = line.indexOf("*");
            int end = line.lastIndexOf("*");

            if (start != -1 && end != -1 && start < end) {

                String before = line.substring(0, start);
                String clickable = line.substring(start + 1, end);
                String after = line.substring(end + 1);

                TextComponent beforeComponent = new TextComponent(before);
                TextComponent clickableComponent = new TextComponent(clickable);
                clickableComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept " + sender.getName()));
                beforeComponent.addExtra(clickableComponent);
                beforeComponent.addExtra(new TextComponent(after));

                alert.add(beforeComponent);

            } else {
                alert.add(new TextComponent(line));
            }
        }
        return alert;
    }

    @Override
    public String[] getRequestHover(Request request) {
        List<String> configList = config.getStringList("request-hover");
        String[] hover = new String[configList.size()];

        for (String s : configList) {
            int index = configList.indexOf(s);
            hover[index] = format(s)
                    .replaceAll("%reason%", request.getReason())
                    .replaceAll("%time%", new SimpleDateFormat("hh:mm:ss a").format(new Date(request.getCreated())))
                    .replaceAll("%name%", main.getUserManager().getUser(request.getPlayerId()).getName());

            if (index!=configList.size()-1) {
                hover[index] = hover[index]+"\n";
            }

        }

        return hover;
    }

    @Override
    public void reload() {
        main.reloadConfig();

        Configuration mainConfig = (Configuration) main.getConfig();

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
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(languageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        usage = null;
    }
}
