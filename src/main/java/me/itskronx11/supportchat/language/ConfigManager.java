package me.itskronx11.supportchat.language;

import lombok.Getter;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ConfigManager {
    @Getter
    protected ConfigurationWrapper config;
    protected SupportMain main;
    public ConfigManager(SupportMain main) {
        this.main = main;
        reload();
    }
    public static String createArgs(int start, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            builder.append(args[i]);
            if (i!=args.length-1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
    public static void checkAndSave(ConfigurationWrapper defaultConfig, ConfigurationWrapper config, File file) {
        for (String key : defaultConfig.getKeys()) {
            if (!config.contains(key)) {
                config.set(key, defaultConfig.get(key));
            }
        }
        config.save(file);
    }

    public final String format(String s) {
        return ChatColor.translateAlternateColorCodes('&',s).replaceAll("%prefix%", getPrefix());
    }
    public String format(User user, String s) {
        return format(s).replaceAll("%name%", user.getName())
                .replaceAll("%reason%", (user.getRequest()==null) ? "" : user.getRequest().getReason())
                .replaceAll("%time%", (user.getRequest()==null) ? "" : new SimpleDateFormat("hh:mm:ss a").format(new Date(user.getRequest().getCreated())));
    }
    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfiguration().getString("prefix"));
    }
    public String getMessage(String path) {
        return format(config.getString(path));
    }
    public String getMessage(User user, String path) {
        return format(user, config.getString(path));
    }
    public List<String> getUsage() {
        List<String> usage = config.getStringList("usage");
        usage.replaceAll(this::format);
        return usage;
    }
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
    public List<String> getRequestHover(Request request) {
        List<String> configList = config.getStringList("request-hover");

        configList.replaceAll(line -> format(main.getUserManager().getUser(request.getPlayerId()), line));

        for (String s : configList) {
            int index = configList.indexOf(s);
            if (index==configList.size()-1) {
                configList.set(index, s+"\n");
            }
        }
        return configList;
    }
    public abstract void reload();
    public boolean usingTitles() {
        return main.getConfiguration().getBoolean("send-titles");
    }
}
