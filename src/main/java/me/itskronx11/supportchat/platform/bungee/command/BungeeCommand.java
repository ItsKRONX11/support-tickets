package me.itskronx11.supportchat.platform.bungee.command;

import me.itskronx11.supportchat.SupportCommand;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommand extends Command {
    private final SupportMain main;
    private final ConfigManager languageManager;
    public BungeeCommand(SupportMain main, ConfigManager languageManager, String name) {
        super(name, null, "sup");

        this.main = main;
        this.languageManager = languageManager;
        command = new SupportCommand(main, languageManager);
    }
    private final SupportCommand command;

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            languageManager.reload();
            commandSender.sendMessage(languageManager.getMessage("config-reload"));
            return;
        }
        User sender = main.getUserManager().getUser(((ProxiedPlayer) commandSender).getUniqueId());

        command.onCommand(sender, strings);
    }
}
