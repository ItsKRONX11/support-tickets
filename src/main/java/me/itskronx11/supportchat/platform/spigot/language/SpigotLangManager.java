package me.itskronx11.supportchat.platform.spigot.language;

import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.user.User;
import me.itskronx11.supportchat.support.Request;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class SpigotLangManager implements ConfigManager {
private final SupportMain main;
private FileConfiguration config;
private String[] usage;
    public SpigotLangManager(SupportMain main) {
        this.main = main;

        reload();
    }
    @Override
    public String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s).replaceAll("%prefix%",getPrefix());
    }
    @Override
    public String format(User user, String s) {
        return format(s).replaceAll("%name%", user.getName());
    }
    @Override
    public String getMessage(String path) {
        return format(config.getString(path));
    }
    @Override
    public String getMessage(User user, String path) {
        return format(user, config.getString(path));
    }
    @Override
    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&',((FileConfiguration)main.getConfig()).getString("prefix"));
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
    public void reload() {
        main.reloadConfig();

        String lang = ((FileConfiguration) main.getConfig()).getString("language");

        if (lang.endsWith(".yml")) {
            lang = lang.split(".yml")[0];
        }

        File file = new File(main.getDataFolder().getPath()+"/language/"+lang+".yml");
        if (!file.exists()) {
            System.err.println("File '"+lang+".yml' does not exist. Using default language configuration: lang_en.yml");
            file = new File(main.getDataFolder(), "language/lang_en.yml");
        }

        config = YamlConfiguration.loadConfiguration(file);

        usage = null;
    }

}
