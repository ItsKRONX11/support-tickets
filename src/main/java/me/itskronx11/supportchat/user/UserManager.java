package me.itskronx11.supportchat.user;

import com.google.common.io.Files;
import com.google.gson.Gson;
import me.itskronx11.supportchat.SupportMain;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class UserManager {
    private final Map<UUID, User> uuidUser;
    private final SupportMain main;
    private final Gson gson = new Gson();
    public UserManager(SupportMain main) {
        uuidUser = new ConcurrentHashMap<>();
        this.main = main;
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
    public void save(User user) {
        save(user.getSaveData());
    }
    private void save(UserData userData) {
        try {
            File folder = new File(main.getDataFolder(), "userdata");
            File file = new File(folder, userData.getUuid().toString()+".json");

            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(new byte[0], file); // empty the file

            Writer writer = new FileWriter(file, false);
            gson.toJson(userData, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public UserData loadUserData(UUID uuid) {
        File folder = new File(main.getDataFolder(), "userdata");
        File file = new File(folder, uuid.toString()+".json");
        if (!file.exists()) {
            return null;
        }
        try {
            return gson.fromJson(new FileReader(file), UserData.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void saveAll() {
        for (User user : this.getUsers()) {
            save(user);
        }
    }
}
