package me.itskronx11.supportchat.user;

import lombok.Getter;

import java.util.UUID;

public class UserData {
    @Getter
    private final UUID uuid;
    @Getter
    private final String name;
    @Getter
    private final boolean chat;
    @Getter
    private final boolean alerts;
    @Getter
    private final int totalSupports;

    public UserData(UUID uuid, String name, boolean chat, boolean alerts, int totalSupports) {
        this.uuid = uuid;
        this.name = name;
        this.chat = chat;
        this.alerts = alerts;
        this.totalSupports = totalSupports;
    }

}
