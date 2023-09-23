package me.itskronx11.supportchat.support;

import lombok.Getter;
import me.itskronx11.supportchat.user.User;

import java.util.UUID;

public class Request {
    @Getter
    private final UUID playerId;
    @Getter
    private final String reason;
    @Getter
    private final long created;
    public Request(User user, String reason, long created) {
        playerId = user.getUniqueId();
        this.reason = reason;
        this.created = created;
    }
}
