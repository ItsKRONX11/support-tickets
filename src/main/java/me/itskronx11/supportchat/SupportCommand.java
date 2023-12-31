package me.itskronx11.supportchat;

import me.itskronx11.supportchat.language.ConfigManager;
import me.itskronx11.supportchat.support.Request;
import me.itskronx11.supportchat.support.Support;
import me.itskronx11.supportchat.user.User;
import net.md_5.bungee.api.chat.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SupportCommand {
    private static final String[] COMMANDS = new String[]{"help", "reload", "request", "join", "leave", "list", "chat", "togglechat", "togglealerts", "add", "remove", "close"};
    private final SupportMain main;
    private final ConfigManager languageManager;

    public SupportCommand(SupportMain main, ConfigManager languageManager) {
        this.main = main;
        this.languageManager = languageManager;
    }

    public List<String> tabComplete(User sender, String[] args) {
        return Arrays.stream(COMMANDS).filter(cmd -> sender.hasPermission("support."+cmd)).filter(cmd -> cmd.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
    }
    public final void onCommand(User sender, String[] args) {
        if (!sender.hasPermission("support.use")) {
            sender.sendMessage(languageManager.getMessage("no-permission"));
            return;
        }
        if (args.length == 0) {
            for (String usageLine : languageManager.getUsage(sender)) {
                sender.sendMessage(usageLine);
            }
            return;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                reload(sender);
                return;

            case "help":
                help(sender);
                return;

            case "request":
                request(sender, args);
                return;

            case "join":
                join(sender, args);
                return;

            case "leave":
                leave(sender);
                return;

            case "togglechat":
                toggleChat(sender);
                return;

            case "togglealerts":
                toggleAlerts(sender);
                return;

            case "list":
                list(sender);
                return;

            case "accept":
                accept(sender, args);
                return;

            case "chat":
                chat(sender, args);
                return;

            case "add":
                add(sender, args);
                return;

            case "remove":
                remove(sender, args);
                return;

            case "close":
                close(sender);
                return;

            default:
                sender.sendMessage(languageManager.getMessage("unknown-command"));
        }

    }

    public boolean checkPermission(User user, String permission) {
        if (!user.hasPermission(permission)) {
            user.sendMessage(languageManager.getMessage("no-permission"));
            return true;
        }
        return false;
    }
    public void reload(User sender) {
        if (checkPermission(sender, "support.reload")) return;

        languageManager.reload();
        sender.sendMessage(languageManager.getMessage("config-reload"));
    }

    public void help(User sender) {
        for (String string : languageManager.getUsage(sender)) {
            sender.sendMessage(string);
        }
    }
    public void request(User sender, String[] args) {
        if (checkPermission(sender, "support.request")) return;

        if (!(args.length>=2)) {
            sender.sendMessage(languageManager.getMessage("command-usage.request"));
            return;
        }
        if (sender.getRequest()!=null) {
            sender.sendMessage(languageManager.getMessage("already-in-queue"));
            return;
        }
        if (sender.getSupport()!=null) {
            sender.sendMessage(languageManager.getMessage("already-in-ticket"));
            return;
        }
        String reason = ConfigManager.createArgs(1, args);
        sender.setRequest(new Request(sender, reason, System.currentTimeMillis()));
        sender.sendMessage(languageManager.getMessage("request-sent"));

        for (User staff : main.getUserManager().getUsers()) {
            if (staff.hasPermission("support.notifications") && staff.isAlertsEnabled()) {
                for (TextComponent component : languageManager.getSupportAlert(sender, reason)) {
                    staff.sendMessage(component);
                }
                if (languageManager.usingTitles()) {
                    List<String> titleContent = languageManager.getConfig().getStringList("support-title.content");
                    staff.sendTitle(
                            languageManager.format(sender, titleContent.get(0)),
                            languageManager.format(sender, titleContent.get(1)),
                            languageManager.getConfig().getInt("support-title.duration.fade-in"),
                            languageManager.getConfig().getInt("support-title.duration.fade-out"),
                            languageManager.getConfig().getInt("support-title.duration.stay")
                    );
                }
            }
        }
    }
    public void leave(User sender) {
        if (checkPermission(sender, "support.leave")) return;

        if (sender.getSupport()!=null) {
            sender.getSupport().removePlayer(sender);
            sender.sendMessage(languageManager.getMessage("left-support"));
            return;
        }
        if (sender.getRequest()!=null) {
            sender.setRequest(null);
            sender.sendMessage(languageManager.getMessage("queue-left"));
            return;
        }
        sender.sendMessage(languageManager.getMessage("not-in-queue"));
    }
    public void toggleChat(User sender) {
        if (checkPermission(sender, "support.togglechat")) return;

        if (sender.isChatEnabled()) {
            sender.setChatEnabled(false);
            sender.sendMessage(languageManager.getMessage("disabled-support-chat"));
        } else {
            sender.setChatEnabled(true);
            sender.sendMessage(languageManager.getMessage("enabled-support-chat"));
        }
    }
    public void toggleAlerts(User sender) {
        if (checkPermission(sender, "support.togglealerts")) return;

        if (sender.isAlertsEnabled()) {
            sender.setAlertsEnabled(false);
            sender.sendMessage(languageManager.getMessage("disabled-alerts"));
            return;
        }
        sender.setAlertsEnabled(true);
        sender.sendMessage(languageManager.getMessage("enabled-alerts"));
    }
    public void list(User sender) {
        if (checkPermission(sender, "support.list")) return;

        sender.sendMessage(languageManager.getMessage("support-list-header"));
        for (User user : main.getUserManager().getUsers()) {
            Request request = user.getRequest();
            if (request==null) continue;

            List<String> hoverString = languageManager.getRequestHover(request);

            BaseComponent[] hoverText = new TextComponent[hoverString.size()];
            for (int i = 0; i < hoverString.size(); i++) {
                hoverText[i] = new TextComponent(hoverString.get(i));
            }

            BaseComponent[] component = new ComponentBuilder(languageManager.getMessage(user,"support-list")).
                    event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept "+user.getName()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))
                    .create();
            sender.sendMessage(component);
        }
    }
    public void accept(User sender, String[] args) {
        if (checkPermission(sender, "support.accept")) return;
        if (sender.getRequest()!=null) {
            sender.sendMessage(languageManager.getMessage("already-in-queue"));
            return;
        }
        if (sender.getSupport()!=null) {
            sender.sendMessage(languageManager.getMessage("already-in-support"));
            return;
        }
        if (args.length!=2) {
            sender.sendMessage(languageManager.getMessage("command-usage.accept"));
            return;
        }
        User target = main.getUserManager().getUser(args[1]);
        if (target==null) {
            sender.sendMessage(languageManager.getMessage("not-online"));
            return;
        }
        if (sender==target) {
            sender.sendMessage(languageManager.getMessage("cannot-self"));
            return;
        }
        if (target.getSupport()!=null) {
            sender.sendMessage(languageManager.getMessage("player-already-in-ticket"));
            return;
        }
        if (target.getRequest()==null) {
            sender.sendMessage(languageManager.getMessage("player-not-in-queue"));
            return;
        }
        target.setRequest(null);
        new Support(target, sender, main);

        for (User user : main.getUserManager().getUsers()) {
            if (user.hasPermission("support.notifications") && user.isAlertsEnabled()) {
                String message = languageManager.getMessage("player-helped").replaceAll("%player%", target.getName()).replaceAll("%staff%", sender.getName());

                if (main.isLuckPerms()) {
                    message = message.replaceAll("%staff_rank%", sender.getRankDisplayName())
                            .replaceAll("%staff_prefix%", sender.getLuckPermsPrefix())
                            .replaceAll("%staff_suffix%", sender.getLuckPermsSuffix())
                            .replaceAll("%player_rank%", target.getRankDisplayName())
                            .replaceAll("%player_prefix%", target.getLuckPermsPrefix())
                            .replaceAll("%player_suffix%", target.getLuckPermsSuffix());
                }

                user.sendMessage(message);
            }
        }
    }
    public void chat(User sender, String[] args) {
        if (checkPermission(sender, "support.chat")) return;

        Support support = sender.getSupport();
        if (support==null) {
            sender.sendMessage(languageManager.getMessage("not-in-ticket"));
            return;
        }
        if (!(args.length>=2)) {
            sender.sendMessage(languageManager.getMessage("command-usage.chat"));
            return;
        }
        String message = ConfigManager.createArgs(1, args);
        support.sendMessage(languageManager.getMessage(sender, "support-chat").replaceAll("%message%",message));
    }
    public void add(User sender, String[] args) {
        if (checkPermission(sender, "support.add")) return;

        Support support = sender.getSupport();
        if (support==null) {
            sender.sendMessage(languageManager.getMessage("not-in-ticket"));
            return;
        }
        if (!(args.length>=2)) {
            sender.sendMessage(languageManager.getMessage("command-usage.add"));
            return;
        }
        User target = main.getUserManager().getUser(args[1]);
        if (target==null) {
            sender.sendMessage(languageManager.getMessage("not-online"));
            return;
        }
        if (target.getSupport()!=null) {
            sender.sendMessage("player-already-in-ticket");
            return;
        }
        target.setRequest(null);
        sender.getSupport().addPlayer(target);
        target.sendMessage(languageManager.getMessage("added-support"));
    }
    public void remove(User sender, String[] args) {
        if (checkPermission(sender, "support.remove")) return;

        if (sender.getSupport()==null) {
            sender.sendMessage(languageManager.getMessage("not-in-ticket"));
            return;
        }
        if (!(args.length>=2)) {
            sender.sendMessage(languageManager.getMessage("command-usage.remove"));
            return;
        }
        User target = main.getUserManager().getUser(args[1]);
        if (target==null) {
            sender.sendMessage(languageManager.getMessage("not-online"));
            return;
        }
        if (target.getSupport()==null) {
            sender.sendMessage("player-not-in-ticket");
            return;
        }
        if (sender.getSupport()!=target.getSupport()) {
            sender.sendMessage(languageManager.getMessage("cannot-remove-elsewhere"));
            return;
        }
        sender.getSupport().removePlayer(target);
        target.sendMessage(languageManager.getMessage("removed-support"));
    }
    public void join(User sender, String[] args) {
        if (checkPermission(sender, "support.join")) return;

        if (sender.getSupport()!=null) {
            sender.sendMessage(languageManager.getMessage("already-in-ticket"));
            return;
        }
        if (sender.getRequest()!=null) {
            sender.sendMessage(languageManager.getMessage("already-in-queue"));
            return;
        }
        User target = main.getUserManager().getUser(args[0]);
        if (target==null) {
            sender.sendMessage(languageManager.getMessage("not-online"));
            return;
        }
        if (target.getSupport()==null) {
            sender.sendMessage(languageManager.getMessage("player-not-in-ticket"));
            return;
        }
        target.getSupport().addPlayer(sender);
    }

    public void close(User sender) {
        if (checkPermission(sender, "support.close")) return;

        if (sender.getSupport()==null) {
            sender.sendMessage(languageManager.getMessage(sender, "not-in-ticket"));
            return;
        }


        sender.getSupport().close(sender);
    }
}
