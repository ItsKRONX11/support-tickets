package me.itskronx11.supportchat.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@AllArgsConstructor
public class UserData {
    @Getter private final UUID uuid;
    @Getter private final String name;
    @Getter private final boolean chat;
    @Getter private final boolean alerts;
    @Getter private final int totalSupports;
}
