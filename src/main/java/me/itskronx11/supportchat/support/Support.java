package me.itskronx11.supportchat.support;

import lombok.Getter;
import me.itskronx11.supportchat.SupportMain;
import me.itskronx11.supportchat.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Support {
    private final List<UUID> userIds;
    @Getter
    private final UUID initiator;
    private final SupportMain main;
    public Support(User helped, User supporter, SupportMain main) {
        userIds = new ArrayList<>();
        this.main = main;

        initiator = helped.getUniqueId(); 

        userIds.add(helped.getUniqueId());
        userIds.add(supporter.getUniqueId());

        helped.setSupport(this);
        supporter.setSupport(this);

        helped.sendMessage(main.getLanguageManager().getMessage(supporter, "helped-player"));
        supporter.sendMessage(main.getLanguageManager().getMessage(helped, "helped-staff"));
    }
    public void close(User whoClosed) {
        for (UUID uuid : userIds) {
            main.getUserManager().getUser(uuid).setSupport(null);
        }
        if (whoClosed!=null) {
            sendMessage(main.getLanguageManager().getMessage(whoClosed, "support-close"));

        }
        userIds.clear();
    }
    public void addPlayer(User user) {
        userIds.add(user.getUniqueId());
        user.setSupport(this);

        sendMessage(main.getLanguageManager().getMessage(user, "player-join-support"));
    }
    public void removePlayer(User user) {
        userIds.remove(user.getUniqueId());
        user.setSupport(null);

        sendMessage(main.getLanguageManager().getMessage(user, "player-left-support"));
        if (this.userIds.size()==0 || this.userIds.size()==1) {
            sendMessage(main.getLanguageManager().getMessage("support-auto-close"));
            this.close(null);
            return;
        }
    }
    public void sendMessage(String message) {
        for (UUID uuid : this.userIds) {
            main.getUserManager().getUser(uuid).sendMessage(message);
        }
    }
}
