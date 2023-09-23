package me.itskronx11.supportchat.language;

import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public interface LanguageManager {
    default String createArgs(int start, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            builder.append(args[i]);
            if (i!=args.length-1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    String format(String s);
    String format(User user, String s);
    String getPrefix();
    String getMessage(String path);
    String getMessage(User user, String path);
    String[] getUsage();
    List<TextComponent> getSupportAlert(User sender, String reason);
    String[] getRequestHover(Request request);
    void reload();

}
