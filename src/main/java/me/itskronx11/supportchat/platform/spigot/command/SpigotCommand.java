package me.itskronx11.supportchat.platform.spigot.command;

import me.itskronx11.supportchat.SupportCommand;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpigotCommand extends SupportCommand implements CommandExecutor {
    private final SupportMain main;
    private final ConfigManager languageManager;

    public SpigotCommand(SupportMain main) {
        super(main, main.getLanguageManager());

        this.main = main;
        languageManager = main.getLanguageManager();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            languageManager.reload();
            commandSender.sendMessage(languageManager.getMessage("config-reload"));
            return true;
        }
        User sender = main.getUserManager().getUser(((Player) commandSender).getUniqueId());

        super.onCommand(sender, args);

        return true;
    }
}