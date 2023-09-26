package me.itskronx11.supportchat.platform.spigot.user;

import com.cryptomorin.xseries.messages.Titles;
import lombok.Getter;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;

import java.util.UUID;

public class SpigotUser extends User {
    private final UUID uuid;
    @Getter
    private final String name;
    public SpigotUser(UUID uuid) {
        super(uuid);
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
    }
    @Override
    public void sendMessage(String message) {
        Bukkit.getPlayer(uuid).sendMessage(message);
    }

    @Override
    public void sendMessage(BaseComponent component) {
        Bukkit.getPlayer(uuid).spigot().sendMessage(component);
    }

    @Override
    public void sendMessage(BaseComponent[] components) {
        Bukkit.getPlayer(uuid).spigot().sendMessage(components);
    }

    @Override
    public boolean hasPermission(String permission) {
        return Bukkit.getPlayer(uuid).hasPermission(permission);
    }

    @Override
    public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        Titles.sendTitle(
                Bukkit.getPlayer(uuid),
                fadeIn,
                stay,
                fadeOut,
                title,
                subTitle);
    }



}
