package me.itskronx11.supportchat.language;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ConfigurationWrapper {
    Object get(String path);
    void set(String path, Object value);
    String getString(String path);
    boolean getBoolean(String path);
    List<String> getStringList(String path);
    int getInt(String path);
    void save(File file);
    Collection<String> getKeys();
    boolean contains(String path);


    class BungeeConfig implements ConfigurationWrapper {
        private final Configuration config;
        public BungeeConfig(Configuration config) {
            this.config = config;
        }
        @Override
        public Object get(String path) {
            return config.get(path);
        }
        @Override
        public void set(String path, Object value) {
            config.set(path, value);
        }
        @Override
        public String getString(String path) {
            return config.getString(path);
        }
        @Override
        public boolean getBoolean(String path) {
            return config.getBoolean(path);
        }
        @Override
        public List<String> getStringList(String path) {
            return config.getStringList(path);
        }

        @Override
        public int getInt(String path) {
            return config.getInt(path);
        }

        @Override
        public void save(File file) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public Collection<String> getKeys() {
            return config.getKeys();
        }

        @Override
        public boolean contains(String path) {
            return config.contains(path);
        }
    }
    class SpigotConfig implements ConfigurationWrapper {
        private final FileConfiguration config;
        public SpigotConfig(FileConfiguration config) {
            this.config = config;
        }
        @Override
        public Object get(String path) {
            return config.get(path);
        }
        @Override
        public void set(String path, Object value) {
            config.set(path, value);
        }
        @Override
        public String getString(String path) {
            return config.getString(path);
        }
        @Override
        public boolean getBoolean(String path) {
            return config.getBoolean(path);
        }
        @Override
        public List<String> getStringList(String path) {
            return config.getStringList(path);
        }

        @Override
        public int getInt(String path) {
            return config.getInt(path);
        }

        @Override
        public void save(File file) {
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Collection<String> getKeys() {
            return config.getKeys(true);
        }

        @Override
        public boolean contains(String path) {
            return config.contains(path);
        }
    }
}
