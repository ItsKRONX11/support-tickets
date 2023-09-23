package me.itskronx11.supportchat.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class UserManager {
    private final Map<UUID, User> uuidUser;
    public UserManager() {
        uuidUser = new HashMap<>();
    }
    public User getUser(UUID uuid) {
        return uuidUser.get(uuid);
    }
    public abstract User getUser(String name);
    public void removeUser(User user) {
        uuidUser.remove(user.getUniqueId());
    }
    public void addUser(User user) {
        uuidUser.put(user.getUniqueId(), user);
    }
    public Collection<User> getUsers() {
        return uuidUser.values();
    }
}
